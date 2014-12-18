/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.rss.client.controllers.standard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.controllers.RssViewControllerBase;

import javax.portlet.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Controller for view mode, display of RSS items.
 *
 * @author Jonas Liljenfeldt
 * @author anders asplund
 * @author David Rosell
 * @author Simon Göransson - Monator Technologies AB
 */
@Controller
@RequestMapping("VIEW")
public class RssViewController extends RssViewControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssViewController.class);
    private static final String FEED = "feed";

    // In certain environments it seems that the fetching is not threadsafe and thus fails when using multiple threads.
    private ExecutorService executor = Executors.newFixedThreadPool(1);
//    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Standard Rss portlet view controller.
     */
    public RssViewController() {
        super.setLogger(LOGGER);
    }

    /**
     * Shows RSS items for user.
     *
     * @param model                ModelMap
     * @param request              RenderRequest
     * @param response             RenderResponse
     * @param preferences          RSS client VIEW portlet's PortletPreferences
     * @param selectedRssItemTitle selectedRssItemTitle
     * @return View name.
     * @throws
     * @throws IOException If I/O problems occur
     */
    @RenderMapping
    public String viewRssItemList(final ModelMap model, final RenderRequest request, RenderResponse response,
                                  final PortletPreferences preferences,
                                  @RequestParam(value = "selectedRssItemTitle", required = false) String selectedRssItemTitle)
            throws IOException {

        addUserToModel(model, request);
        addTabStateToModel(model, request, preferences);
        addTabTitleToModel(model, request, preferences);


        // Check for sort order or pre-selection (no fetch on default load, this to avoid "page lock")
        // Comparator<FeedEntryBean> sortOrder = getSortOrder(model);
        // if (sortOrder != null || selectedRssItemTitle != null) {
        ResourceBundle bundle = getPortletConfig().getResourceBundle(response.getLocale());

        String rssFeedPref = getRssFeedPref(request);
        List<String> rssFeedPrefs = new ArrayList<String>();
        if ((Boolean) model.get("isTab1Active")) {
            rssFeedPrefs.add(PortletPreferencesWrapperBean.RSS_FEED_LINK_1);
        }
        if ((Boolean) model.get("isTab2Active")) {
            rssFeedPrefs.add(PortletPreferencesWrapperBean.RSS_FEED_LINK_2);
        }
        if ((Boolean) model.get("isTab3Active")) {
            rssFeedPrefs.add(PortletPreferencesWrapperBean.RSS_FEED_LINK_3);
        }
        if ((Boolean) model.get("isTab4Active")) {
            rssFeedPrefs.add(PortletPreferencesWrapperBean.RSS_FEED_LINK_4);
        }

        // Fetch the feeds in parallel
        List<List<FeedEntryBean>> listOfRssEntriesLists = new ArrayList<List<FeedEntryBean>>();
        for (String feedPref : rssFeedPrefs) {
            final String feedPrefTemp = feedPref;
            listOfRssEntriesLists.add(new FutureWrapperList(executor.submit(new Callable<List<FeedEntryBean>>() {
                @Override
                public List<FeedEntryBean> call() throws Exception {
                    List<FeedEntryBean> sortedRssEntries = getSortedAndFilteredItemsForOneFeedLink(model, request,
                            preferences, feedPrefTemp);

                    return sortedRssEntries;
                }
            })));
        }

        response.setContentType("text/html");
        model.addAttribute("listOfRssEntriesLists", listOfRssEntriesLists);

        model.addAttribute("selectedRssItemTitle", selectedRssItemTitle);
        return "rssFeedView";
    }

    private List<FeedEntryBean> getSortedAndFilteredItemsForOneFeedLink(ModelMap model, RenderRequest request,
                                                                        PortletPreferences preferences,
                                                                        String rssFeedPref) throws IOException {
        List<FeedEntryBean> sortedRssEntries =
                getSortedRssEntries(preferences, model, rssFeedPref);

        sortedRssEntries =
                getItemsToBeDisplayed(preferences, sortedRssEntries, getRssFeedNumberOfItemsPref(request));
        return sortedRssEntries;
    }

    /**
     * @param model
     * @param request
     * @param preferences
     */
    private void addTabTitleToModel(ModelMap model, RenderRequest request, PortletPreferences preferences) {
        List<String> rssFeedTitles = new ArrayList<String>();

        if ((Boolean) model.get("isTab1Active")) {
            rssFeedTitles.add(preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_1, null));
        }
        if ((Boolean) model.get("isTab2Active")) {
            rssFeedTitles.add(preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_2, null));
        }
        if ((Boolean) model.get("isTab3Active")) {
            rssFeedTitles.add(preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_3, null));
        }
        if ((Boolean) model.get("isTab4Active")) {
            rssFeedTitles.add(preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_4, null));
        }

        model.addAttribute("rssFeedTitles", rssFeedTitles);
    }

    /**
     * @param model
     * @param request
     */
    private void addTabStateToModel(ModelMap model, RenderRequest request, PortletPreferences preferences) {

        boolean isTab1Active = false;
        boolean isTab2Active = false;
        boolean isTab3Active = false;
        boolean isTab4Active = false;

        String rssFeedLink1 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_1, "");
        String rssFeedLink2 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_2, "");
        String rssFeedLink3 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_3, "");
        String rssFeedLink4 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_4, "");

        if (rssFeedLink1 != null && !rssFeedLink1.equals("")) {
            isTab1Active = true;
        }
        if (rssFeedLink2 != null && !rssFeedLink2.equals("")) {
            isTab2Active = true;
        }
        if (rssFeedLink3 != null && !rssFeedLink3.equals("")) {
            isTab3Active = true;
        }
        if (rssFeedLink4 != null && !rssFeedLink4.equals("")) {
            isTab4Active = true;
        }

        model.addAttribute("isTab1Active", isTab1Active);
        model.addAttribute("isTab2Active", isTab2Active);
        model.addAttribute("isTab3Active", isTab3Active);
        model.addAttribute("isTab4Active", isTab4Active);
    }

    /**
     * @param request
     * @return
     */
    private String getRssFeedPref(RenderRequest request) {
        String feed = request.getParameter(FEED);
        if (feed != null) {
            if (feed.equals("2")) {
                return PortletPreferencesWrapperBean.RSS_FEED_LINK_2;
            }
            if (feed.equals("3")) {
                return PortletPreferencesWrapperBean.RSS_FEED_LINK_3;
            }
            if (feed.equals("4")) {
                return PortletPreferencesWrapperBean.RSS_FEED_LINK_4;
            }
        }
        return PortletPreferencesWrapperBean.RSS_FEED_LINK_1;
    }

    /**
     * @param request
     * @return
     */
    private String getRssFeedNumberOfItemsPref(RenderRequest request) {
        String feed = request.getParameter(FEED);
        if (feed != null) {
            if (feed.equals("2")) {
                return PortletPreferencesWrapperBean.NUMBER_OF_ITEM_2;
            }
            if (feed.equals("3")) {
                return PortletPreferencesWrapperBean.NUMBER_OF_ITEM_3;
            }
            if (feed.equals("4")) {
                return PortletPreferencesWrapperBean.NUMBER_OF_ITEM_4;
            }
        }
        return PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1;
    }

    /**
     * Prepare to show feed entries view by adding RSS entries sorted by date to model.
     *
     * @param model       The model
     * @param request     The portlet request
     * @param response    The portlet response
     * @param preferences The portlet preferences
     * @return The name of the view to display
     * @throws IOException If I/O problems occur
     */
    @ResourceMapping("sortByDate")
    public String viewFeedEntriesByDate(ModelMap model, ResourceRequest request, ResourceResponse response,
                                        PortletPreferences preferences) throws IOException {

        addUserToModel(model, request);

        setSortOrderByDate(model);
        return "rssItems";
    }

    /**
     * Prepare to show feed entries view by adding RSS entries sorted by feed title to model.
     *
     * @param model       The model
     * @param request     The portlet request
     * @param response    The portlet response
     * @param preferences The portlet preferences
     * @return The name of the view to display
     * @throws IOException If I/O problems occur
     */
    @ResourceMapping("groupBySource")
    public String viewFeedEntriesBySource(ModelMap model, ResourceRequest request, ResourceResponse response,
                                          PortletPreferences preferences) throws IOException {

        addUserToModel(model, request);

        setSortOrderByFeedTitle(model);
        return "rssItems";
    }

    /**
     * Set the sort order of the RSS items to date (descending).
     *
     * @param model The model
     */
    @ActionMapping("sortByDate")
    public void setSortOrderByDate(ModelMap model) {
        model.addAttribute(SORT_ORDER, FeedEntryBean.SORT_BY_DATE);
    }

    /**
     * Set the sort order of the RSS items to feed title (ascending).
     *
     * @param model The model
     */
    @ActionMapping("groupBySource")
    public void setSortOrderByFeedTitle(ModelMap model) {
        model.addAttribute(SORT_ORDER, FeedEntryBean.GROUP_BY_SOURCE);
    }

    private class FutureWrapperList extends AbstractList<FeedEntryBean> {

        private Future<List<FeedEntryBean>> internalList;

        public FutureWrapperList(Future<List<FeedEntryBean>> submit) {
            internalList = submit;
        }

        @Override
        public FeedEntryBean get(int index) {
            try {
                return internalList.get(30, TimeUnit.SECONDS).get(index);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }

        @Override
        public int size() {
            try {
                return internalList.get(30, TimeUnit.SECONDS).size();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                LOGGER.error(e.getMessage(), e);
                return 0;
            }
        }
    }
}