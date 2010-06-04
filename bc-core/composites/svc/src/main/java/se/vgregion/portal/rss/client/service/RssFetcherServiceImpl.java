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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author jonas liljenfeldt
 * 
 */
@Service
public class RssFetcherServiceImpl implements RssFetcherService {

    @Override
    public List<SyndFeed> getRssFeeds(String[] feedUrlsArray) throws IllegalArgumentException, IOException,
            FeedException {
        List<SyndFeed> syndFeeds = new ArrayList<SyndFeed>();
        // String[] feedLinks = new String[] {"http://localhost:8000/vgr.rss"};
        for (String feedLink : feedUrlsArray) {
            if (!StringUtils.isBlank(feedLink)) {
                URL feedUrl = new URL(feedLink);
                // TODO Should be handled in cooperation with pubsubhub if URL contains "pubsubhub.vgregion.se"
                SyndFeedInput syndFeedInput = new SyndFeedInput();
                syndFeeds.add(syndFeedInput.build(new XmlReader(feedUrl)));
            }
        }
        return syndFeeds;
    }

    /**
     * Used for testing only.
     * 
     * @param args
     * @throws FeedException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws FeedException, IllegalArgumentException, IOException {
        List<SyndFeed> rssItemList =
                new RssFetcherServiceImpl().getRssFeeds(new String[] {"http://localhost:8000/vgr.rss"});
        for (SyndFeed syndFeed : rssItemList) {
            List<SyndEntry> items = syndFeed.getEntries();
            for (SyndEntry item : items) {
                System.out.println(item.getPublishedDate() + " " + item.getTitle());
                System.out.println(item.getDescription().getValue());
            }
        }
    }
}