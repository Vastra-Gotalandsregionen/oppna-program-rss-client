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
package se.vgregion.portal.rss.client.controllers.minimal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.ui.ModelMap;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.service.RssFetcherService;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class RssMinimalViewControllerTest {

    private RssMinimalViewController rssMinimalViewController;

    @Mock
    private RssFetcherService mockRssFetcherService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        rssMinimalViewController = new RssMinimalViewController();

        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.controllers.minimal.RssMinimalViewController#viewRssItemList(org.springframework.ui.ModelMap, javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.PortletPreferences)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testViewRssItemList() throws IOException {
        MockPortletConfig mockPortletConfig = new MockPortletConfig();
        MockRenderRequest mockRenderRequest = new MockRenderRequest();
        MockRenderResponse mockRenderResponse = new MockRenderResponse();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        ModelMap modelMap = new ModelMap();

        rssMinimalViewController.setPortletConfig(mockPortletConfig);
        rssMinimalViewController.setRssFetcherService(mockRssFetcherService);

        assertEquals("rssFeedViewMinimal", rssMinimalViewController.viewRssItemList(modelMap, mockRenderRequest,
                mockRenderResponse, mockPortletPreferences));
        assertEquals(0, ((List<FeedEntryBean>) modelMap.get("rssEntries")).size());
    }
}
