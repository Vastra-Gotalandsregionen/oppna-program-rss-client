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

import javax.portlet.ReadOnlyException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator;
import se.vgregion.portal.rss.client.controllers.RssEditControllerBase;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import com.sun.syndication.fetcher.FeedFetcher;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class RssMinimalEditControllerTest {

    RssMinimalEditController rssMinimalEditController;

    @Mock
    private FeedFetcher feedFetcher;

    @Mock
    private RssFetcherService rssFetcherService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        rssMinimalEditController = new RssMinimalEditController(rssFetcherService);

        PortletPreferencesWrapperBeanValidator validator = new PortletPreferencesWrapperBeanValidator(feedFetcher);

        ReflectionTestUtils.setField(rssMinimalEditController, "validator", validator);
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.controllers.minimal.RssMinimalEditController#showPreferences(org.springframework.ui.ModelMap, javax.portlet.PortletPreferences, se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean, org.springframework.validation.BindingResult)}
     * .
     * 
     * @throws ReadOnlyException
     */
    @Test
    public final void testShowPreferences() throws ReadOnlyException {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");

        assertEquals(rssMinimalEditController.getConfigJsp(), rssMinimalEditController.showPreferences(modelMap,
                mockPortletPreferences, preferencesBean, bindingResult));

        PortletPreferencesWrapperBean pb = (PortletPreferencesWrapperBean) modelMap
                .get(RssEditControllerBase.PREFERENCES);

        assertSame(preferencesBean, pb);
        assertEquals(PortletPreferencesWrapperBean.DEFAULT_NUMBER_OF_EXCERPT_ROWS,
                Integer.parseInt(pb.getNumberOfExcerptRows()));
        assertEquals(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS,
                Integer.parseInt(pb.getNumberOfItems()));
    }

    @Test
    public final void testShowPreferencesException() throws ReadOnlyException {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");

        modelMap.put("saveError", "true");

        assertEquals(RssEditControllerBase.ERROR_JSP, rssMinimalEditController.showPreferences(modelMap,
                mockPortletPreferences, preferencesBean, bindingResult));
    }
}
