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

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

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

    /**
     * Standard Rss portlet view controller.
     */
    public RssViewController() {
        super.setLogger(LOGGER);
    }

    /**
     * Shows RSS items for user.
     * 
     * @param model
     *            ModelMap
     * @param request
     *            RenderRequest
     * @param response
     *            RenderResponse
     * @param preferences
     *            RSS client VIEW portlet's PortletPreferences
     * @param selectedRssItemTitle
     *            selectedRssItemTitle
     * @return View name.
     * @throws
     * @throws IOException
     *             If I/O problems occur
     */
    @RenderMapping
    public String viewRssItemList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences,
            @RequestParam(value = "selectedRssItemTitle", required = false) String selectedRssItemTitle)
            throws IOException {

        addUserToModel(model, request);

        // Check for sort order or pre-selection (no fetch on default load, this to avoid "page lock")
        // Comparator<FeedEntryBean> sortOrder = getSortOrder(model);
        // if (sortOrder != null || selectedRssItemTitle != null) {
        ResourceBundle bundle = getPortletConfig().getResourceBundle(response.getLocale());

        List<FeedEntryBean> sortedRssEntries =
                getSortedRssEntries(preferences, model, getRssFeedPref(request));

        sortedRssEntries =
                getItemsToBeDisplayed(preferences, sortedRssEntries, getRssFeedNumberOfItemsPref(request));

        if (bundle != null) {
            response.setTitle(bundle.getString("javax.portlet.title") + " (" + sortedRssEntries.size() + ")");
        }

        addTabStateToModel(model, request, preferences);
        addTabTitleToModel(model, request, preferences);
        
        String rssFeedTitle = "RSS";
        
        System.out.println("FLAG - sortedRssEntries is null: " + (sortedRssEntries == null));
        System.out.println("FLAG - sortedRssEntries size: " + sortedRssEntries.size());
        
        if(sortedRssEntries.size() > 0) {
        	rssFeedTitle = sortedRssEntries.get(0).getFeedTitle();
        }
        
        /*
        if(sortedRssEntries.size() > 0) {
        	rssFeedTitle = sortedRssEntries.get(0).getFeedTitle();
        }
        */

        response.setContentType("text/html");
        model.addAttribute("rssEntries", sortedRssEntries);
        model.addAttribute("rssFeedLink", preferences.getValue(getRssFeedPref(request), ""));
        model.addAttribute("rssFeedTitle", rssFeedTitle);

        // }
        model.addAttribute("selectedRssItemTitle", selectedRssItemTitle);
        return "rssFeedView";
    }

    /**
     * @param model
     * @param request
     * @param preferences
     */
    private void addTabTitleToModel(ModelMap model, RenderRequest request, PortletPreferences preferences) {
        model.addAttribute("rssFeedTitle1",
                preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_1, ""));
        model.addAttribute("rssFeedTitle2",
                preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_2, ""));
        model.addAttribute("rssFeedTitle3",
                preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_3, ""));
        model.addAttribute("rssFeedTitle4",
                preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_TITLE_4, ""));
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
     * @param model
     *            The model
     * @param request
     *            The portlet request
     * @param response
     *            The portlet response
     * @param preferences
     *            The portlet preferences
     * @return The name of the view to display
     * @throws IOException
     *             If I/O problems occur
     */
    @ResourceMapping("sortByDate")
    public String viewFeedEntriesByDate(ModelMap model, ResourceRequest request, ResourceResponse response,
            PortletPreferences preferences) throws IOException {

        addUserToModel(model, request);

        setSortOrderByDate(model);
        // addSortedFeedEntriesToModel(model, preferences, PortletPreferencesWrapperBean.RSS_FEED_LINK_1,
        // PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1);
        return "rssItems";
    }

    /**
     * Prepare to show feed entries view by adding RSS entries sorted by feed title to model.
     * 
     * @param model
     *            The model
     * @param request
     *            The portlet request
     * @param response
     *            The portlet response
     * @param preferences
     *            The portlet preferences
     * @return The name of the view to display
     * @throws IOException
     *             If I/O problems occur
     */
    @ResourceMapping("groupBySource")
    public String viewFeedEntriesBySource(ModelMap model, ResourceRequest request, ResourceResponse response,
            PortletPreferences preferences) throws IOException {

        addUserToModel(model, request);

        setSortOrderByFeedTitle(model);
        // addSortedFeedEntriesToModel(model, preferences, PortletPreferencesWrapperBean.RSS_FEED_LINK_1,
        // PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1);
        return "rssItems";
    }

    /**
     * Set the sort order of the RSS items to date (descending).
     * 
     * @param model
     *            The model
     */
    @ActionMapping("sortByDate")
    public void setSortOrderByDate(ModelMap model) {
        // System.out.println("RssViewController.setSortOrderByDate()");
        model.addAttribute(SORT_ORDER, FeedEntryBean.SORT_BY_DATE);
    }

    /**
     * Set the sort order of the RSS items to feed title (ascending).
     * 
     * @param model
     *            The model
     */
    @ActionMapping("groupBySource")
    public void setSortOrderByFeedTitle(ModelMap model) {
        // System.out.println("RssViewController.setSortOrderBySource()");
        model.addAttribute(SORT_ORDER, FeedEntryBean.GROUP_BY_SOURCE);
    }
}