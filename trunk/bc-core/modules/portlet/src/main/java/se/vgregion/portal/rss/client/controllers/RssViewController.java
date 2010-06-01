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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

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
        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());

        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);

        // Get list of URLs for user saved in his/her preferences
        String feedUrls = preferences.getValue(RssEditController.CONFIG_RSS_FEED_LINK_KEY, "");
        String[] feedUrlsArray = null;
        if (feedUrls != null) {
            feedUrlsArray = feedUrls.split("\n");
        }

        List<FeedEntryBean> feedEntryBeans = null;
        if (feedUrlsArray != null && feedUrlsArray.length > 0) {
            try {
                List<SyndFeed> rssFeeds = rssFetcherService.getRssFeeds(feedUrlsArray);
                feedEntryBeans = getFeedEntries(rssFeeds);

//                for (FeedEntryBean rssFeedBean : feedEntryBeans) {
//                    System.out.println(rssFeedBean.getFeedTitle() + ": " + rssFeedBean.getExcerpt());
//                }
                // Set number of RSS items in RSS viewer portlet header title.
                if (bundle != null) {
                    response.setTitle(bundle.getString("javax.portlet.title") + " (" + feedEntryBeans.size() + ")");
                }
            } catch (FeedException e) {
                LOGGER.error("Error when trying to fetch RSS items for user " + userId + ".", e);
            }
        }

        model.addAttribute("rssEntries", feedEntryBeans);
        return "rssFeedView";
    }

    private List<FeedEntryBean> getFeedEntries(List<SyndFeed> rssFeeds) {
        List<FeedEntryBean> feedEntryBeans = new ArrayList<FeedEntryBean>();
        for (SyndFeed syndFeed : rssFeeds) {
            for (int i = 0; syndFeed.getEntries() != null && i < syndFeed.getEntries().size(); i++) {
                feedEntryBeans.add(new FeedEntryBean((SyndEntry) syndFeed.getEntries().get(i), syndFeed.getTitle()));
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