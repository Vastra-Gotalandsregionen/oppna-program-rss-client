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
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.ConnectException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rometools.fetcher.FeedFetcher;
import org.springframework.test.util.ReflectionTestUtils;
import se.vgregion.portal.rss.blacklist.BlackList;
import se.vgregion.portal.rss.blacklist.ConcurrentBlackList;

import com.sun.syndication.feed.synd.SyndFeed;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RssFetcherServiceImplTest {

    @Mock
    private CustomHttpURLFeedFetcher feedFetcher;
    @Mock
    private SyndFeed syndFeed;
    @Mock
    FeedException feedException;
    BlackList<String> blackList;

    private RssFetcherServiceImpl rssFetcherService;
    private Set<String> testFeeds;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        blackList = new ConcurrentBlackList<String>();
        rssFetcherService = new RssFetcherServiceImpl(feedFetcher, blackList);
        testFeeds = new HashSet<String>(Arrays.asList("http://www.swedroid.se/feed",
                "http://feeds.feedburner.com/UbuntuGeek"));
    }

    @Test
    public void shouldGetRssFeeds() throws Exception {
        // Given
        given(feedFetcher.retrieveFeed(any(URL.class))).willReturn(syndFeed);

        // When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertEquals(testFeeds.size(), syndFeeds.size());
    }

    @Test(expected = FeedException.class)
    public void shouldThrowFeedExceptionIfInvalidXml() throws Exception {
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(feedException);

        // When
        rssFetcherService.getRssFeeds(testFeeds);
    }

    @Test
    public void shouldReturnEmptyFeedListIfNoUrlsGiven() throws Exception {
        // Given
        testFeeds = Collections.emptySet();

        // When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertTrue(syndFeeds.isEmpty());

    }

    @Test
    public void shouldReturnEmptyFeedListIfEmptyUrlGiven() throws Exception {
        // Given
        testFeeds = new HashSet<String>(Arrays.asList(" "));

        // When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertTrue(syndFeeds.isEmpty());

    }

    @Test
    public void shouldAddUrlToBlackListIfConnectionExceptionThrown() throws Exception {
        // Given
        testFeeds = new HashSet<String>(Arrays.asList("http://url.com"));
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException("error"));

        // When
        rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertEquals(1, blackList.items().size());
    }

    @Test
    public void shouldSkipUrlInBlackList() throws Exception {
        // Given
        testFeeds = new HashSet<String>(Arrays.asList("http://url.com"));
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException("error"));
        rssFetcherService.getRssFeeds(testFeeds);

        // When
        rssFetcherService.getRssFeeds(testFeeds);

        // Then
        verify(feedFetcher, times(1)).retrieveFeed(any(URL.class));
    }

}
