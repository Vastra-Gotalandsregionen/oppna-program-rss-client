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

/**
 * 
 */
package se.vgregion.portal.rss.client.controllers.standard;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ReadOnlyException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.mock.web.portlet.MockResourceRequest;
import org.springframework.mock.web.portlet.MockResourceResponse;
import org.springframework.ui.ModelMap;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class RssViewControllerTest {

    private RssViewController rssViewController;

    @Mock
    private RssFetcherService mockRssFetcherService;

    @Mock
    private SyndFeed syndFeed;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        rssViewController = new RssViewController();

        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.controllers.standard.RssViewController#viewRssItemList(org.springframework.ui.ModelMap, javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.PortletPreferences, java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testViewRssItemListDefault() throws IOException {
        MockPortletConfig mockPortletConfig = new MockPortletConfig();
        MockRenderRequest mockRenderRequest = new MockRenderRequest();
        MockRenderResponse mockRenderResponse = new MockRenderResponse();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        ModelMap modelMap = new ModelMap();

        rssViewController.setPortletConfig(mockPortletConfig);
        rssViewController.setRssFetcherService(mockRssFetcherService);

        assertEquals("rssFeedView", rssViewController.viewRssItemList(modelMap, mockRenderRequest,
                mockRenderResponse, mockPortletPreferences, "TheTitle"));
        assertEquals("TheTitle", modelMap.get("selectedRssItemTitle"));
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.controllers.standard.RssViewController#viewRssItemList(org.springframework.ui.ModelMap, javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.PortletPreferences, java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testViewRssItemListSortOrderSet() throws IOException {
        MockPortletConfig mockPortletConfig = new MockPortletConfig();
        MockRenderRequest mockRenderRequest = new MockRenderRequest();
        MockRenderResponse mockRenderResponse = new MockRenderResponse();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        ModelMap modelMap = new ModelMap();

        rssViewController.setSortOrderByDate(modelMap);
        rssViewController.setPortletConfig(mockPortletConfig);
        rssViewController.setRssFetcherService(mockRssFetcherService);

        assertEquals("rssFeedView", rssViewController.viewRssItemList(modelMap, mockRenderRequest,
                mockRenderResponse, mockPortletPreferences, "TheTitle"));
        assertEquals(0, ((List<FeedEntryBean>) modelMap.get("rssEntries")).size());
        assertEquals("TheTitle", modelMap.get("selectedRssItemTitle"));
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.controllers.standard.RssViewController#viewFeedEntriesByDate(org.springframework.ui.ModelMap, javax.portlet.ResourceRequest, javax.portlet.ResourceResponse, javax.portlet.PortletPreferences)}
     * .
     * 
     * @throws IOException
     * @throws ReadOnlyException
     * @throws FeedException
     * @throws FetcherException
     * @throws IllegalArgumentException
     */
    @Test
    public final void testViewFeedEntriesByDate() throws IOException, ReadOnlyException, FeedException,
            IllegalArgumentException, FetcherException {
        MockResourceRequest mockResourceRequest = new MockResourceRequest();
        MockResourceResponse mockResourceResponse = new MockResourceResponse();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        ModelMap modelMap = new ModelMap();

        mockPortletPreferences.setValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "http://vgregion.se");

        List<SyndFeed> list = new ArrayList<SyndFeed>();
        list.add(syndFeed);

        SyndEntry syndEntry1 = new SyndEntryImpl();
        syndEntry1.setTitle("T1");
        syndEntry1.setPublishedDate(new Date(1));
        SyndEntry syndEntry2 = new SyndEntryImpl();
        syndEntry2.setTitle("T2");
        syndEntry2.setPublishedDate(new Date(2));
        SyndEntry syndEntry3 = new SyndEntryImpl();
        syndEntry3.setTitle("T3");
        syndEntry3.setPublishedDate(new Date(3));

        List<SyndEntry> listEntry = new ArrayList<SyndEntry>();
        listEntry.add(syndEntry1);
        listEntry.add(syndEntry3);
        listEntry.add(syndEntry2);

        given(syndFeed.getTitle()).willReturn("Title");
        given(syndFeed.getEntries()).willReturn(listEntry);
        given(mockRssFetcherService.getRssFeeds(new String[] { "http://vgregion.se" })).willReturn(list);

        rssViewController.setRssFetcherService(mockRssFetcherService);

        rssViewController.viewFeedEntriesByDate(modelMap, mockResourceRequest, mockResourceResponse,
                mockPortletPreferences);

        List<FeedEntryBean> feedEntries = (List<FeedEntryBean>) modelMap.get("rssEntries");
        assertEquals("Title", feedEntries.get(0).getFeedTitle());

        assertEquals(3, feedEntries.size());

        assertEquals("T1", feedEntries.get(2).getTitle());
        assertEquals("T2", feedEntries.get(1).getTitle());
        assertEquals("T3", feedEntries.get(0).getTitle());

        assertEquals(FeedEntryBean.SORT_BY_DATE, modelMap.get(RssViewController.SORT_ORDER));
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.controllers.standard.RssViewController#viewFeedEntriesBySource(org.springframework.ui.ModelMap, javax.portlet.ResourceRequest, javax.portlet.ResourceResponse, javax.portlet.PortletPreferences)}
     * .
     * 
     * @throws IOException
     * @throws FeedException
     * @throws ReadOnlyException
     * @throws FetcherException
     * @throws IllegalArgumentException
     */
    @Test
    public final void testViewFeedEntriesBySource() throws IOException, FeedException, ReadOnlyException,
            IllegalArgumentException, FetcherException {
        MockResourceRequest mockResourceRequest = new MockResourceRequest();
        MockResourceResponse mockResourceResponse = new MockResourceResponse();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        ModelMap modelMap = new ModelMap();

        mockPortletPreferences.setValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS,
                "http://vgregion1.se\nhttp://vgregion2.se");

        rssViewController.setRssFetcherService(mockRssFetcherService);

        SyndFeed syndFeed1 = new SyndFeedImpl();
        syndFeed1.setTitle("FT1");
        SyndFeed syndFeed2 = new SyndFeedImpl();
        syndFeed2.setTitle("FT2");

        List<SyndFeed> list = new ArrayList<SyndFeed>();
        list.add(syndFeed1);
        list.add(syndFeed2);

        SyndEntry syndEntry1 = new SyndEntryImpl();
        syndEntry1.setTitle("T1");
        syndEntry1.setPublishedDate(new Date(1));
        SyndEntry syndEntry2 = new SyndEntryImpl();
        syndEntry2.setTitle("T2");
        syndEntry2.setPublishedDate(new Date(2));
        SyndEntry syndEntry3 = new SyndEntryImpl();
        syndEntry3.setTitle("T3");
        syndEntry3.setPublishedDate(new Date(3));

        syndFeed1.getEntries().add(syndEntry1);
        syndFeed2.getEntries().add(syndEntry2);
        syndFeed1.getEntries().add(syndEntry3);

        given(mockRssFetcherService.getRssFeeds(new String[] { "http://vgregion1.se", "http://vgregion2.se" }))
                .willReturn(list);

        rssViewController.viewFeedEntriesBySource(modelMap, mockResourceRequest, mockResourceResponse,
                mockPortletPreferences);

        List<FeedEntryBean> feedEntries = (List<FeedEntryBean>) modelMap.get("rssEntries");

        assertEquals("FT1", feedEntries.get(0).getFeedTitle());
        assertEquals("FT1", feedEntries.get(1).getFeedTitle());
        assertEquals("FT2", feedEntries.get(2).getFeedTitle());

        assertEquals(FeedEntryBean.GROUP_BY_SOURCE, modelMap.get(RssViewController.SORT_ORDER));
    }
}
