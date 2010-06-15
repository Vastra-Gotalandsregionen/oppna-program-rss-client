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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssFetcherServiceImplTest {

    @Mock
    private SyndFeedInput syndFeedInput;

    @Mock
    private SyndFeed syndFeed;
    
    private RssFetcherServiceImpl rssFetcherService;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        rssFetcherService = new RssFetcherServiceImpl();
        rssFetcherService.setSyndFeedInput(syndFeedInput);
    }

    @Test
    public void shouldGetRssFeeds() throws Exception {
        //Given
        String[] testFeeds =
            new String[] {"http://www.swedroid.se/feed", "http://feeds.feedburner.com/UbuntuGeek"};
        given(syndFeedInput.build(any(XmlReader.class))).willReturn(syndFeed);
        
        //When
        List<SyndFeed> syndFeeds = rssFetcherService.getRssFeeds(testFeeds);

        //Then
        assertEquals(testFeeds.length, syndFeeds.size());
    }
}
