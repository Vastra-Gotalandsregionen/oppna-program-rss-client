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

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.ConnectException;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.io.FeedException;

public class RssFetcherServiceImplTest {

    @Mock
    private FeedFetcher feedFetcher;
    @Mock
    private SyndFeed syndFeed;
    @Mock
    FeedException feedException;
    @Mock
    PropertiesUtil propertiesUtil;

    private RssFetcherServiceImpl rssFetcherService;
    private String[] testFeeds;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        rssFetcherService = new RssFetcherServiceImpl(feedFetcher, propertiesUtil);
        testFeeds = new String[] { "http://www.swedroid.se/feed", "http://feeds.feedburner.com/UbuntuGeek" };
    }

    @Test
    public void shouldGetRssFeeds() throws Exception {
        // Given
        given(feedFetcher.retrieveFeed(any(URL.class))).willReturn(syndFeed);

        // When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertEquals(testFeeds.length, syndFeeds.size());
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
        testFeeds = new String[] {};

        // When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertTrue(syndFeeds.isEmpty());

    }

    @Test
    public void shouldReturnEmptyFeedListIfEmptyUrlGiven() throws Exception {
        // Given
        testFeeds = new String[] { " " };

        // When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertTrue(syndFeeds.isEmpty());

    }

    @Test
    public void shouldAddUrlToBlackListIfConnectionExceptionThrown() throws Exception {
        // Given
        testFeeds = new String[] { "http://url.com" };
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException("error"));

        // When
        rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertEquals(1, rssFetcherService.getFeedBlackList().size());
    }

    @Test
    public void shouldSkipUrlInBlackList() throws Exception {
        // Given
        testFeeds = new String[] { "http://url.com" };
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException("error"));
        rssFetcherService.getRssFeeds(testFeeds);

        // When
        rssFetcherService.getRssFeeds(testFeeds);

        // Then
        verify(feedFetcher, times(1)).retrieveFeed(any(URL.class));
    }

    @Test
    public void shouldClearBlackList() throws Exception {
        // Given
        testFeeds = new String[] { "http://url.com", "http://url2.com" };
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException("error"));

        // When
        rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertEquals(2, rssFetcherService.getFeedBlackList().size());

        // When
        rssFetcherService.clearFeedBlackList();

        // Then
        assertEquals(0, rssFetcherService.getFeedBlackList().size());
    }

    @Test
    public void shouldRemoveUrlFromBlackList() throws Exception {
        // Given
        testFeeds = new String[] { "http://url.com", "http://url2.com" };
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException("error"));

        // When
        rssFetcherService.getRssFeeds(testFeeds);

        // Then
        assertEquals(2, rssFetcherService.getFeedBlackList().size());

        // When
        rssFetcherService.removeFromFeedBlackList("http://url.com");

        // Then
        assertEquals(1, rssFetcherService.getFeedBlackList().size());
        assertEquals("http://url2.com", rssFetcherService.getFeedBlackList().get(0));
    }
}
