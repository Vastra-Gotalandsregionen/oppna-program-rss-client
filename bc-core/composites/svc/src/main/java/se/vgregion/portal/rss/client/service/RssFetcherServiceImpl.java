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
import java.util.List;

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
    public SyndFeed getRssFeed(String userId) throws IllegalArgumentException, IOException, FeedException {
        URL feedUrl = new URL("http://localhost:8000/vgr.rss");
        SyndFeedInput syndFeedInput = new SyndFeedInput();
        return syndFeedInput.build(new XmlReader(feedUrl));
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws FeedException, IllegalArgumentException, IOException {
        SyndFeed rssItemList = new RssFetcherServiceImpl().getRssFeed("");
        List<SyndEntry> items = rssItemList.getEntries();
        for (SyndEntry item : items) {
            System.out.println(item.getPublishedDate() + " " + item.getTitle());
            System.out.println(item.getDescription().getValue());
        }
    }
    
}
