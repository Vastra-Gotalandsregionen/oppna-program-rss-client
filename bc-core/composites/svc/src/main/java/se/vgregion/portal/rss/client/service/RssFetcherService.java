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
import org.rometools.fetcher.FetcherException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for fetching RSS feeds.
 *
 * @author jonas liljenfeldt
 * @author Anders Asplund - Callista Enterprise
 */
public interface RssFetcherService {

    /**
     * Returns a list of SyndFeed generated by an array of feed urls.
     *
     * @param feedUrls set of rss feed urls
     * @return a list of Rome SyndFeeds generated by the list of urls
     * @throws IOException      an I/O exception has occurred
     * @throws FeedException    an Feed exception has occurred
     * @throws FetcherException an FetcherException has occurred
     */
    List<SyndFeed> getRssFeeds(Set<String> feedUrls) throws FeedException, IOException,
            FetcherException;

    List<Map<String, String>> getAllInformationFromLatestFeed();
}