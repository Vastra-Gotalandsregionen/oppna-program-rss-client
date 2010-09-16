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

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.chain.StringTemplatePlaceholderProcessor;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import java.io.IOException;
import java.util.*;

/**
 * Controller base for view mode, display of RSS items.
 * 
 * @author Jonas Liljenfeldt
 * @author Anders Asplund
 */
public class RssViewControllerBase {

    private Logger logger = LoggerFactory.getLogger(RssViewControllerBase.class);

    public static final String SORT_ORDER = "sort_order";

    @Autowired
    protected RssFetcherService rssFetcherService = null;

    @Autowired
    protected PortletConfig portletConfig = null;

    @Autowired
    private StringTemplatePlaceholderProcessor templateProcessor = null;

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    @SuppressWarnings("unchecked")
    protected Comparator<FeedEntryBean> getSortOrder(ModelMap model) {
        Comparator<FeedEntryBean> comparator = (Comparator<FeedEntryBean>) model.get(SORT_ORDER);
        return comparator;
    }

    protected List<FeedEntryBean> getRssEntries(PortletPreferences preferences) throws IOException {
        Set<String> feedUrls = getFeedUrls(preferences);
        List<FeedEntryBean> feedEntries = Collections.emptyList();
        try {

            feedEntries = getFeedEntries(rssFetcherService.getRssFeeds(feedUrls));
        } catch (FeedException e) {
            logger.error("Error when trying to fetch RSS items: " + feedUrls, e);
            e.printStackTrace();
        } catch (FetcherException e) {
            logger.error("Error when trying to fetch RSS items: " + feedUrls, e);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            logger.error("Error when trying to fetch RSS items: " + feedUrls, e);
            e.printStackTrace();
        }
        return feedEntries;
    }

    protected void addSortedFeedEntriesToModel(ModelMap model, PortletPreferences preferences) throws IOException {
        List<FeedEntryBean> feedEntries = getSortedRssEntries(model, preferences);
        feedEntries = getItemsToBeDisplayed(preferences, feedEntries);
        model.addAttribute("rssEntries", feedEntries);
    }

    protected Set<String> getFeedUrls(PortletPreferences preferences) {
        Set<String> feedUrls = new HashSet<String>();

        // Get list of URLs for user saved in his/her preferences
        String feedUrlTemplates = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "");
        if (feedUrls != null) {
            for (String feedUrl : Arrays.asList(feedUrlTemplates.split("\n"))) {
                Set<String> processedFeedUrls = templateProcessor.replacePlaceholders(feedUrl, "bruno");
                feedUrls.addAll(processedFeedUrls);
            }
        }

        System.out.println(feedUrls.toString());
        return feedUrls;
    }

    protected List<FeedEntryBean> getFeedEntries(List<SyndFeed> rssFeeds) {
        List<FeedEntryBean> feedEntryBeans = new ArrayList<FeedEntryBean>();
        for (int j = 0; j < rssFeeds.size(); j++) {
            SyndFeed syndFeed = rssFeeds.get(j);
            for (int i = 0; syndFeed.getEntries() != null && i < syndFeed.getEntries().size(); i++) {
                feedEntryBeans
                        .add(new FeedEntryBean((SyndEntry) syndFeed.getEntries().get(i), syndFeed.getTitle()));
            }
        }
        return feedEntryBeans;
    }

    protected List<FeedEntryBean> getItemsToBeDisplayed(PortletPreferences preferences,
            List<FeedEntryBean> sortedRssEntries) {
        sortedRssEntries = sortedRssEntries.subList(
                0,
                Math.min(
                        sortedRssEntries.size(),
                        Integer.valueOf(preferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS,
                                String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)))));
        return sortedRssEntries;
    }

    protected List<FeedEntryBean> getSortedRssEntries(ModelMap model, PortletPreferences preferences)
            throws IOException {
        List<FeedEntryBean> feedEntries = getRssEntries(preferences);
        Collections.sort(feedEntries, getSortOrder(model));
        return feedEntries;
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public void setRssFetcherService(RssFetcherService rssFetcherService) {
        this.rssFetcherService = rssFetcherService;
    }

    public void setTemplateProcessor(StringTemplatePlaceholderProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }
}