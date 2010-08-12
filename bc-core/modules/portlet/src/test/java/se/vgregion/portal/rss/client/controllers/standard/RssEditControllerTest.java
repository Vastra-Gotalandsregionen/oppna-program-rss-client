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
import java.util.Arrays;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator;
import se.vgregion.portal.rss.client.controllers.RssEditControllerBase;
import se.vgregion.portal.rss.client.service.RssFetcherService;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class RssEditControllerTest {
    RssEditController rssEditController;

    @Mock
    PortletPreferencesWrapperBeanValidator mockValidator;

    @Mock
    PortletPreferencesWrapperBean mockPreferencesBean;

    @Mock
    RssFetcherService rssFetcherService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        rssEditController = new RssEditController(rssFetcherService);

        ReflectionTestUtils.setField(rssEditController, "validator", mockValidator);
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

        assertEquals(rssEditController.getConfigJsp(), rssEditController.showPreferences(modelMap,
                mockPortletPreferences, preferencesBean, bindingResult));

        PortletPreferencesWrapperBean pb = (PortletPreferencesWrapperBean) modelMap
                .get(RssEditController.PREFERENCES);

        assertSame(preferencesBean, pb);
        assertEquals(PortletPreferencesWrapperBean.DEFAULT_NUMBER_OF_EXCERPT_ROWS,
                Integer.parseInt(pb.getNumberOfExcerptRows()));
        assertEquals(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS,
                Integer.parseInt(pb.getNumberOfItems()));
    }

    @Test
    public final void testCopyValidationErrorsFromRender() throws ReadOnlyException {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");
        BindingResult errorResult = new BeanPropertyBindingResult(preferencesBean, "command");
        errorResult.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl",
                new Object[] { "link" }, "Ogiltig adress");
        modelMap.put("errors", errorResult);

        rssEditController.showPreferences(modelMap, mockPortletPreferences, preferencesBean, bindingResult);

        assertEquals(1, bindingResult.getErrorCount());
        assertEquals("link", bindingResult.getFieldError(PortletPreferencesWrapperBean.RSS_FEED_LINKS)
                .getArguments()[0]);
    }

    @Test
    public final void testShowPreferencesInBlackList() throws ReadOnlyException {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        mockPortletPreferences.setValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "link1\nlink2");
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");

        given(rssFetcherService.getFeedBlackList()).willReturn(Arrays.asList("link1", "link3"));

        rssEditController.showPreferences(modelMap, mockPortletPreferences, preferencesBean, bindingResult);

        PortletPreferencesWrapperBean pb = (PortletPreferencesWrapperBean) modelMap
                .get(RssEditController.PREFERENCES);

        assertEquals(1, pb.getFeedBlackList().size());
        assertEquals("link1", pb.getFeedBlackList().get(0));
    }

    @Test
    public final void testShowPreferencesException() throws ReadOnlyException {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");

        modelMap.put("saveError", "true");

        assertEquals(RssEditControllerBase.ERROR_JSP, rssEditController.showPreferences(modelMap,
                mockPortletPreferences, preferencesBean, bindingResult));
    }

    @Test
    public final void testSavePreferencesOK() {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");

        rssEditController.savePreferences(modelMap, mockPortletPreferences, preferencesBean, bindingResult);

        assertNull(modelMap.get("saveError"));
        assertNull(modelMap.get("errors"));
    }

    @Test
    public final void testSavePreferencesValidationFailed() {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        PortletPreferencesWrapperBean preferencesBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(preferencesBean, "command");

        // Faking validation error
        bindingResult.addError(new ObjectError("A", "B"));
        // given(mockValidator.validate(target, errors)).willThrow(new ParsingFeedException("error"));

        rssEditController.savePreferences(modelMap, mockPortletPreferences, preferencesBean, bindingResult);

        assertNull(modelMap.get("saveError"));
        assertNotNull(modelMap.get("errors"));
    }

    @Test
    public final void testSavePreferencesStoreException() throws ReadOnlyException, ValidatorException,
            IOException {
        ModelMap modelMap = new ModelMap();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();
        BindingResult bindingResult = new BeanPropertyBindingResult(mockPreferencesBean, "command");

        willThrow(new ReadOnlyException("error")).given(mockPreferencesBean).store(mockPortletPreferences);

        rssEditController.savePreferences(modelMap, mockPortletPreferences, mockPreferencesBean, bindingResult);

        assertEquals("true", modelMap.get("saveError"));
        assertNull(modelMap.get("errors"));
    }
}
