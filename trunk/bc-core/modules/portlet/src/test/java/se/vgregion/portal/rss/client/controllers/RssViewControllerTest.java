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

package se.vgregion.portal.rss.client.controllers;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class RssViewControllerTest {

    @Mock
    private ModelMap model;

    @Mock
    private RenderRequest renderRequest;

    @Mock
    private ActionRequest actionRequest;

    @Mock
    private RenderResponse renderResponse;

    @Mock
    private ActionResponse actionResponse;

    @Mock
    private PortletPreferences portletPreferences;

    @Mock
    private List<FeedEntryBean> sortedRssEntries;

    @Mock
    private List<SyndFeed> syndFeeds;

    @Mock
    private SyndFeed syndFeed;

    @Mock
    private PortletConfig portletConfig;
    
    @Mock
    private ResourceBundle resourceBundle;
    
    @Mock
    private RssFetcherService rssFetcherService;
    
    @Mock
    private List feedEntries;
    
    @Mock
    private SyndEntry syndEntry;
    
    @Mock
    private Comparator<FeedEntryBean> comparator;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testViewRssItemList() throws Exception {
        // Given
        RssViewController rssViewController = new RssViewController();
        rssViewController.setPortletConfig(portletConfig);
        rssViewController.setRssFetcherService(rssFetcherService);
        given(model.get("rssEntries")).willReturn(sortedRssEntries);
        given(renderResponse.getLocale()).willReturn(Locale.ENGLISH);
        given(portletConfig.getResourceBundle(any(Locale.class))).willReturn(resourceBundle);
        given(rssFetcherService.getRssFeeds(any(String[].class))).willReturn(syndFeeds);
        given(syndFeeds.size()).willReturn(1);
        given(syndFeeds.get(0)).willReturn(syndFeed);
        given(syndFeed.getEntries()).willReturn(feedEntries);
        given(feedEntries.size()).willReturn(1);
        given(feedEntries.get(0)).willReturn(syndEntry);
        given(syndFeed.getTitle()).willReturn("title");
        given(model.get("sort_order")).willReturn(comparator);
        
        // When
        rssViewController.viewRssItemList(model, renderRequest, renderResponse, portletPreferences);
        
        // Then
        // verify(model, times(1)).addAttribute(anyString(), any(ModelMap.class));
        
        // TODO NB! To be completed a rainy day...
    }

    @Test
    public void testGetSortedRssEntries() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetRssEntries() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetFeedEntriesByDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetFeedEntriesBySource() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetSortOrderByDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetSortOrderByFeedTitle() {
        fail("Not yet implemented");
    }

}
