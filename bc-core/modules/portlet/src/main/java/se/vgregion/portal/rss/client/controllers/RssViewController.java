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

package se.vgregion.portal.rss.client.controllers;

import java.io.IOException;
import java.util.*;

import javax.portlet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

/**
 * Displays RSS items.
 * 
 * @author Jonas Liljenfeldt
 */
@Controller
@RequestMapping("VIEW")
public class RssViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssViewController.class);

    private static final String SORT_ORDER = "sort_order";

    @Autowired
    private RssFetcherService rssFetcherService = null;

    @Autowired
    private PortletConfig portletConfig = null;

    /**
     * Shows RSS items for user.
     * 
     * @param model ModelMap
     * @param request RenderRequest
     * @param response RenderResponse
     * @param preferences RSS client VIEW portlet's PortletPreferences
     * @return View name.
     * @throws
     * @throws IOException
     * @throws IllegalArgumentException
     */
    @RenderMapping
    public String viewRssItemList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) throws IllegalArgumentException, IOException {

        List<FeedEntryBean> sortedRssEntries = Collections.emptyList();
        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());
        sortedRssEntries = getSortedRssEntries(model, preferences);
        sortedRssEntries =
                sortedRssEntries.subList(0, Math.min(sortedRssEntries.size(), Integer.valueOf(preferences
                        .getValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS, String
                                .valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)))));

        if (bundle != null) {
            response.setTitle(bundle.getString("javax.portlet.title") + " (" + sortedRssEntries.size() + ")");
        }
        response.setContentType("text/html");
        model.addAttribute("rssEntries", sortedRssEntries);
        return "rssFeedView";
    }

    public List<FeedEntryBean> getSortedRssEntries(ModelMap model, PortletPreferences preferences)
            throws IllegalArgumentException, IOException {
        List<FeedEntryBean> feedEntries = getRssEntries(preferences);
        Collections.sort(feedEntries, getSortOrder(model));
        return feedEntries;
    }

    @SuppressWarnings("unchecked")
    private Comparator<FeedEntryBean> getSortOrder(ModelMap model) {
        Comparator<FeedEntryBean> comparator = (Comparator<FeedEntryBean>) model.get(SORT_ORDER);
        return comparator;
    }

    public List<FeedEntryBean> getRssEntries(PortletPreferences preferences) throws IllegalArgumentException,
            IOException {
        String[] feedUrls = getFeedUrls(preferences);
        List<FeedEntryBean> feedEntries = Collections.emptyList();
        try {
            feedEntries = getFeedEntries(rssFetcherService.getRssFeeds(feedUrls));
        } catch (FeedException e) {
            LOGGER.error("Error when trying to fetch RSS items: " + Arrays.toString(feedUrls), e);
            e.printStackTrace();
        }
        return feedEntries;
    }

    @ResourceMapping("sortByDate")
    public String getFeedEntriesByDate(ModelMap model, ResourceRequest request, ResourceResponse response,
            PortletPreferences preferences) throws IOException {
        setSortOrderByDate(model);
        return addSortedFeedEntriesToModel(model, preferences);
    }

    @ResourceMapping("groupBySource")
    public String getFeedEntriesBySource(ModelMap model, ResourceRequest request, ResourceResponse response,
            PortletPreferences preferences) throws IOException {
        setSortOrderByFeedTitle(model);
        return addSortedFeedEntriesToModel(model, preferences);
    }

    private String addSortedFeedEntriesToModel(ModelMap model, PortletPreferences preferences) throws IOException {
        List<FeedEntryBean> feedEntries = getSortedRssEntries(model, preferences);
        feedEntries =
                feedEntries.subList(0, Math.min(feedEntries.size(), Integer.valueOf(preferences.getValue(
                        PortletPreferencesWrapperBean.NUMBER_OF_ITEMS, String
                                .valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)))));
        model.addAttribute("rssEntries", feedEntries);
        return "rssItems";
    }

    @ActionMapping("sortByDate")
    public void setSortOrderByDate(ModelMap model) {
        model.addAttribute(SORT_ORDER, FeedEntryBean.SORT_BY_DATE);
    }

    @ActionMapping("groupBySource")
    public void setSortOrderByFeedTitle(ModelMap model) {
        model.addAttribute(SORT_ORDER, FeedEntryBean.GROUP_BY_SOURCE);
    }

    private String[] getFeedUrls(PortletPreferences preferences) {
        // Get list of URLs for user saved in his/her preferences
        String feedUrls = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "");
        String[] feedUrlsArray = null;
        if (feedUrls != null) {
            feedUrlsArray = feedUrls.split("\n");
        }
        return feedUrlsArray;
    }

    private List<FeedEntryBean> getFeedEntries(List<SyndFeed> rssFeeds) {
        List<FeedEntryBean> feedEntryBeans = new ArrayList<FeedEntryBean>();
        for (SyndFeed syndFeed : rssFeeds) {
            for (int i = 0; syndFeed.getEntries() != null && i < syndFeed.getEntries().size(); i++) {
                feedEntryBeans
                        .add(new FeedEntryBean((SyndEntry) syndFeed.getEntries().get(i), syndFeed.getTitle()));
            }
        }
        return feedEntryBeans;
    }
}