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
import com.sun.syndication.io.FeedException;
import org.apache.commons.lang.StringUtils;
import org.rometools.fetcher.FeedFetcher;
import org.rometools.fetcher.FetcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.vgregion.portal.rss.blacklist.BlackList;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link RssFetcherService}.
 *
 * @author jonas liljenfeldt
 * @author anders asplund
 */
@Service
public class RssFetcherServiceImpl implements RssFetcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssFetcherServiceImpl.class);

    private final BlackList<String> feedBlackList;

    private final CustomHttpURLFeedFetcher feedFetcher;

    /**
     * This RssFetcher service keeps track of a feed fetcher and a blacklist.
     *
     * @param feedFetcher the feedFetcher
     * @param blackList the blackList
     */
    @Autowired
    public RssFetcherServiceImpl(CustomHttpURLFeedFetcher feedFetcher, BlackList<String> blackList) {
        this.feedFetcher = feedFetcher;
        feedBlackList = blackList;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws FetcherException
     */
    @Override
    @Cacheable(value = "feedCache")
    public List<SyndFeed> getRssFeeds(Set<String> feedUrls) throws FeedException, IOException,
            FetcherException {
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
                    } else {
                        LOGGER.warn("Feedlink {" + feedLink + "} is blacklisted. Skipping...");
                    }
                }
            }
        }

        /*
         * for (SyndFeed syndFeed : syndFeeds) {
         * 
         * List<SyndEntry> syndEntries = syndFeed.getEntries();
         * 
         * for (SyndEntry syndEntry : syndEntries) { LOGGER.debug("Title " + syndEntry.getTitle());
         * LOGGER.debug("Enclosures " + syndEntry.getEnclosures()); LOGGER.debug("Description " +
         * syndEntry.getDescription()); LOGGER.debug("Source " + syndEntry.getSource()); } }
         */

        return syndFeeds;
    }

    @Override
    public List<Map<String, String>> getAllInformationFromLatestFeed() {
        return feedFetcher.getAllInformationFromLatestFeed().get();
    }
}