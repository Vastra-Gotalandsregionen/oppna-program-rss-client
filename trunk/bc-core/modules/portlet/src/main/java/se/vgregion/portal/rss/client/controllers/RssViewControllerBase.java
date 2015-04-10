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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.rometools.fetcher.FetcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.chain.StringTemplatePlaceholderProcessor;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

/**
 * Controller base for view mode, display of RSS items.
 * 
 * @author Jonas Liljenfeldt
 * @author Anders Asplund
 * @author David Rosell
 * @author Simon Göransson - Monator Technologies AB
 */
public class RssViewControllerBase {

    private Logger logger = LoggerFactory.getLogger(RssViewControllerBase.class);

    /**
     * Placeholder.
     */
    public static final String SORT_ORDER = "sort_order";

    /**
     * RssFetcherService.
     */
    @Autowired
    private RssFetcherService rssFetcherService = null;

    /**
     * PortletConfig.
     */
    @Autowired
    private PortletConfig portletConfig = null;

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

    protected List<FeedEntryBean> getRssEntries(PortletPreferences preferences, ModelMap model,
            String rssFeedPref) throws IOException {
        Set<String> feedUrls = getFeedUrls(preferences, model, rssFeedPref);
        List<FeedEntryBean> feedEntries = Collections.emptyList();
        int noOfRows =
                Integer.parseInt(preferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_EXCERPT_ROWS,
                        String.valueOf(PortletPreferencesWrapperBean.DEFAULT_NUMBER_OF_EXCERPT_ROWS)));
        try {

            final int charactersPerRow = 80; // Roughly
            feedEntries = getFeedEntries(rssFetcherService.getRssFeeds(feedUrls), noOfRows * charactersPerRow);
            
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

        /*
         * for (FeedEntryBean feedEntryBean : feedEntries) { System.out.println("titel !!! = " +
         * feedEntryBean.getTitle()); System.out.println("titel !!! = " + feedEntryBean.getContentsString());
         * System.out.println("titel !!! = " + feedEntryBean.getExcerpt());
         * 
         * for (String s : feedEntryBean.getContents()) { System.out.println("s !!! = " + s); } }
         */

        return feedEntries;
    }

    protected void addSortedFeedEntriesToModel(ModelMap model, PortletPreferences preferences,
            String rssFeedPref, String rssFeedNumberOfItemsPref) throws IOException {
        List<FeedEntryBean> feedEntries = getSortedRssEntries(preferences, model, rssFeedPref);
        feedEntries = getItemsToBeDisplayed(preferences, feedEntries, rssFeedNumberOfItemsPref);
        model.addAttribute("rssEntries", feedEntries);
    }

    protected Set<String> getFeedUrls(PortletPreferences preferences, ModelMap model, String rssFeedPref) {
        Set<String> feedUrls = new HashSet<String>();

        for (String key : new ArrayList<String>(model.keySet())) {
            logger.debug("Model key: "+key);
        }

        // Get list of URLs for user saved in his/her preferences
        String feedUrlTemplates = preferences.getValue(rssFeedPref, "");
        if (feedUrlTemplates == null) {
            //feedUrlTemplates = preferences.getValue(rssFeedPref, "");
            feedUrlTemplates = "";
        }

        if (feedUrls != null) {
            for (String feedUrl : Arrays.asList(feedUrlTemplates.split("\n"))) {
                Long uid = (Long) model.get("uid");
                Set<String> processedFeedUrls =
                        templateProcessor.replacePlaceholders(feedUrl, uid.longValue());

                feedUrls.addAll(processedFeedUrls);
            }
        }

        // System.out.println(feedUrls.toString());
        return feedUrls;
    }

    protected List<FeedEntryBean> getFeedEntries(List<SyndFeed> rssFeeds, int excerptLen) {
        List<FeedEntryBean> feedEntryBeans = new ArrayList<FeedEntryBean>();
        for (int j = 0; j < rssFeeds.size(); j++) {
            SyndFeed syndFeed = rssFeeds.get(j);
            if (syndFeed != null) {
                for (int i = 0; syndFeed.getEntries() != null && i < syndFeed.getEntries().size(); i++) {
                    feedEntryBeans.add(new FeedEntryBean((SyndEntry) syndFeed.getEntries().get(i), syndFeed
                            .getTitle(), excerptLen));
                }
            }
        }
        return feedEntryBeans;
    }

    protected List<FeedEntryBean> getItemsToBeDisplayed(PortletPreferences preferences,
            List<FeedEntryBean> sortedRssEntries, String rssFeedNumberOfItemsPref) {
        sortedRssEntries =
                sortedRssEntries.subList(0, Math.min(
                        sortedRssEntries.size(),
                        Integer.valueOf(preferences.getValue(rssFeedNumberOfItemsPref,
                                String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)))));
        return sortedRssEntries;
    }

    protected List<FeedEntryBean> getSortedRssEntries(PortletPreferences preferences, ModelMap model,
            String rssFeedPref) throws IOException {
        List<FeedEntryBean> feedEntries = getRssEntries(preferences, model, rssFeedPref);
        Collections.sort(feedEntries, getSortOrder(model));
        return feedEntries;
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public PortletConfig getPortletConfig() {
        return portletConfig;
    }

    public void setRssFetcherService(RssFetcherService rssFetcherService) {
        this.rssFetcherService = rssFetcherService;
    }

    public RssFetcherService getRssFetcherService() {
        return rssFetcherService;
    }

    public void setTemplateProcessor(StringTemplatePlaceholderProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    public StringTemplatePlaceholderProcessor getTemplateProcessor() {
        return templateProcessor;
    }

    protected void addUserToModel(ModelMap model, PortletRequest request) {
        long uid = lookupUid(request);
        model.addAttribute("uid", uid);
    }

    protected long lookupUid(PortletRequest req) {
        ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        if (themeDisplay != null) {
            return themeDisplay.getUserId();
        } else if (req.getAttribute("LIFERAY_SHARED_THEME_DISPLAY") != null) {
            return ((ThemeDisplay) req.getAttribute("LIFERAY_SHARED_THEME_DISPLAY")).getUserId();
        } else {
            return (Long) req.getAttribute("USER_ID");
        }
    }
}