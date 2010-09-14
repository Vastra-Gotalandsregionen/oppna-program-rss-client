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

package se.vgregion.portal.rss.client.service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;

/**
 * @author jonas liljenfeldt
 * @author anders asplund
 */
@Service
public class RssFetcherServiceImpl implements RssFetcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssFetcherServiceImpl.class);

    private final List<String> feedBlackList;

    private final FeedFetcher feedFetcher;

    private final PropertiesUtil propertiesUtil;

    @Autowired
    public RssFetcherServiceImpl(FeedFetcher feedFetcher, PropertiesUtil propertiesUtil) {
        this.feedFetcher = feedFetcher;
        this.propertiesUtil = propertiesUtil;
        feedBlackList = Collections.synchronizedList(new ArrayList<String>());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws FetcherException
     * @throws IllegalArgumentException
     */
    @Override
    public List<SyndFeed> getRssFeeds(String[] feedUrlsArray) throws FeedException, IOException,
            IllegalArgumentException, FetcherException {
        List<SyndFeed> syndFeeds = new ArrayList<SyndFeed>();
        if (feedUrlsArray != null) {
            for (String feedLink : feedUrlsArray) {
                // No feed link?
                if (!StringUtils.isBlank(feedLink)) {
                    Set<String> urls = StringTools.replace(feedLink, "${role-query-string}", new HashSet<String>(
                            Arrays.asList("role1", "role2")), propertiesUtil.getPropertiesMap());
                    for (String url : urls) {
                        // Continue if feed is black listed
                        if (!feedBlackList.contains(url)) {
                            try {
                                URL feedUrl = new URL(url);
                                SyndFeed syndFeed = feedFetcher.retrieveFeed(feedUrl);
                                syndFeeds.add(syndFeed);
                            } catch (ConnectException e) {
                                feedBlackList.add(feedLink);
                                LOGGER.error("Failed to fetch feed [{}], received error [{}]", feedLink,
                                        e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return syndFeeds;
    }

    /**
     * {@inheritDoc}
     */
    public void clearFeedBlackList() {
        feedBlackList.clear();
    }

    /**
     * {@inheritDoc}
     */
    public void removeFromFeedBlackList(String feedLink) {
        feedBlackList.remove(feedLink);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getFeedBlackList() {
        return Collections.unmodifiableList(feedBlackList);
    }
}