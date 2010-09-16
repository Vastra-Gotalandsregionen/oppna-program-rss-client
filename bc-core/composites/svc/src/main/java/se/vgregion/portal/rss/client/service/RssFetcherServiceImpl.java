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

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public List<SyndFeed> getRssFeeds(Set<String> feedUrls) throws FeedException, IOException,
            IllegalArgumentException, FetcherException {
        List<SyndFeed> syndFeeds = new ArrayList<SyndFeed>();
        if (feedUrls != null) {
            for (String feedLink : feedUrls) {
                // No feed link?
                if (!StringUtils.isBlank(feedLink)) {
                    // Continue if feed is black listed
                    if (!feedBlackList.contains(feedLink)) {
                        try {
                            URL feedUrl = new URL(feedLink);
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