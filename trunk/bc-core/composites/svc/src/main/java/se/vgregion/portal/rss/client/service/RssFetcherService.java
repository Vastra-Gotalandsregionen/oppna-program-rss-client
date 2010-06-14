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
import java.net.MalformedURLException;
import java.util.List;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

/**
 * @author jonas liljenfeldt
 *
 */
public interface RssFetcherService {

    /**
     * @param feedUrlsArray
     * @return
     * @throws FeedException 
     * @throws MalformedURLException 
     * @throws IOException 
     * @throws IllegalArgumentException 
     * @throws FeedException 
     */
    public List<SyndFeed> getRssFeeds(String[] feedUrlsArray) throws IllegalArgumentException, IOException, FeedException;
}