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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author jonas liljenfeldt
 * @author anders asplund
 */
@Service
public class RssFetcherServiceImpl implements RssFetcherService {

    private SyndFeedInput syndFeedInput;

    @Autowired
    public void setSyndFeedInput(SyndFeedInput syndFeedInput) {
        this.syndFeedInput = syndFeedInput;
    }

    @Override
    public List<SyndFeed> getRssFeeds(String[] feedUrlsArray) throws IllegalArgumentException, IOException,
            FeedException {
        List<SyndFeed> syndFeeds = new ArrayList<SyndFeed>();
        for (String feedLink : feedUrlsArray) {
            if (!StringUtils.isBlank(feedLink)) {
                URL feedUrl = new URL(feedLink);
                // TODO Should be handled in cooperation with pubsubhub if URL contains "pubsubhub.vgregion.se"
                SyndFeed syndFeed = syndFeedInput.build(new XmlReader(feedUrl));
                syndFeeds.add(syndFeed);
            }
        }
        return syndFeeds;
    }
}