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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
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
    @RenderMapping()
    public String viewRssItemList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) throws IllegalArgumentException, IOException {

        String userId = getUserId(request);

        try {
            sortFeedEntriesAndAddToModel(model, response, preferences);
        } catch (FeedException e) {
            LOGGER.error("Error when trying to fetch RSS items for user " + userId + ".", e);
        }
        return "rssFeedView";
    }

    private String getUserId(PortletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);
        return userId;
    }

    @ResourceMapping("sortByDate")
    public String getFeedEntriesByDate(ModelMap model, PortletPreferences preferences, ResourceRequest request) throws IOException {
        String userId = getUserId(request);
        sortFeedEntriesByDate(model);
        String[] feedUrls = getFeedUrls(preferences);
        List<FeedEntryBean> feedEntries = Collections.emptyList();
        try {
            feedEntries = getSortedFeedEntries(model, feedUrls);
        } catch (FeedException e) {
            LOGGER.error("Error when trying to fetch RSS items for user " + userId + ".", e);
        }
        //return feedEntries;
        return "list of feed entries";
    }
    
    @ActionMapping("sortByDate")
    public void sortFeedEntriesByDate(ModelMap model) {
        System.out.println("RssViewController.setFeedEntriesByDate()");
        model.addAttribute(SORT_ORDER, null);
    }

    @ActionMapping("groupBySource")
    public void sortFeedEntriesByFeedTitle(ModelMap model) {
        System.out.println("RssViewController.setFeedEntriesByFeedTitle()");
        model.addAttribute(SORT_ORDER, FeedEntryBean.GROUP_BY_SOURCE);
    }

    @SuppressWarnings("unchecked")
    private void sortFeedEntriesAndAddToModel(ModelMap model, RenderResponse response,
            PortletPreferences preferences) throws IllegalArgumentException, IOException, FeedException {

        String[] feedUrlsArray = getFeedUrls(preferences);

        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());

        List<FeedEntryBean> feedEntries = getSortedFeedEntries(model, feedUrlsArray);
        model.addAttribute("rssEntries", feedEntries);

        if (bundle != null) {
            response.setTitle(bundle.getString("javax.portlet.title") + " (" + feedEntries.size() + ")");
        }
    }

    private String[] getFeedUrls(PortletPreferences preferences) {
        // Get list of URLs for user saved in his/her preferences
        String feedUrls = preferences.getValue(RssEditController.CONFIG_RSS_FEED_LINK_KEY, "");
        String[] feedUrlsArray = null;
        if (feedUrls != null) {
            feedUrlsArray = feedUrls.split("\n");
        }
        return feedUrlsArray;
    }

    private List<FeedEntryBean> getSortedFeedEntries(ModelMap model, String[] feedUrlsArray) throws IOException,
            FeedException {
        List<SyndFeed> rssFeeds = Collections.emptyList();
        if (!ArrayUtils.isEmpty(feedUrlsArray)) {
            rssFeeds = rssFetcherService.getRssFeeds(feedUrlsArray);
        }

        List<FeedEntryBean> feedEntries = getFeedEntries(rssFeeds);
        Collections.sort(feedEntries, (Comparator<FeedEntryBean>) model.get(SORT_ORDER));
        return feedEntries;
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

    private String getUserId(Map<String, ?> attributes) {
        String userId = "";
        if (attributes != null) {
            userId = (String) attributes.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }
        return userId;
    }
}