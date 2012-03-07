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
package se.vgregion.portal.rss.client.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class PortletPreferencesWrapperBeanValidatorTest {

    PortletPreferencesWrapperBeanValidator portletPreferencesWrapperBeanValidator;

    private final FeedFetcher feedFetcher = new HttpURLFeedFetcher();

    @Mock
    private FeedFetcher feedFetcherMock;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        portletPreferencesWrapperBeanValidator = new PortletPreferencesWrapperBeanValidator(feedFetcher);
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator#supports(java.lang.Class)}
     * .
     */
    @Test
    public final void testSupports() {
        assertTrue(portletPreferencesWrapperBeanValidator.supports(PortletPreferencesWrapperBean.class));
        assertFalse(portletPreferencesWrapperBeanValidator.supports(Integer.class));
        assertFalse(portletPreferencesWrapperBeanValidator.supports(MySub.class));
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator#validate(java.lang.Object, org.springframework.validation.Errors)}
     * .
     */
    @Test
    public final void testInvalidNumberOfItems() {
        PortletPreferencesWrapperBean portletPreferencesWrapperBean = new PortletPreferencesWrapperBean();

        BindingResult bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");

        portletPreferencesWrapperBean.setNumberOfItems1("1A2");
        portletPreferencesWrapperBeanValidator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(1, bindingResult.getErrorCount());

        bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");
        portletPreferencesWrapperBean.setNumberOfItems1(null);
        portletPreferencesWrapperBeanValidator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(1, bindingResult.getErrorCount());

        bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");
        portletPreferencesWrapperBean.setNumberOfItems1("1 1");
        portletPreferencesWrapperBeanValidator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(1, bindingResult.getErrorCount());
    }

    @Test
    public final void testInvalidFeedUrls() throws Exception {
        PortletPreferencesWrapperBean portletPreferencesWrapperBean = new PortletPreferencesWrapperBean();

        BindingResult bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");

        portletPreferencesWrapperBean.setRssFeedLink1("abcd");
        portletPreferencesWrapperBeanValidator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(1, bindingResult.getErrorCount());

        bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");
        portletPreferencesWrapperBean.setRssFeedLink1("http://vgregion.se");
        portletPreferencesWrapperBeanValidator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(1, bindingResult.getErrorCount());

        bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");
        portletPreferencesWrapperBean.setRssFeedLink1("http://vgregion.se/feed.xml");
        portletPreferencesWrapperBeanValidator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(1, bindingResult.getErrorCount());
    }

    @Test
    public final void testFetcherException() throws IllegalArgumentException, FeedException, IOException,
            FetcherException {
        testMockedException(new FetcherException("error"));
    }

    @Test
    public final void testParsingFeedException() throws IllegalArgumentException, FeedException, IOException,
            FetcherException {
        testMockedException(new ParsingFeedException("error"));
    }

    @Test
    public final void testFeedException() throws IllegalArgumentException, FeedException, IOException,
            FetcherException {
        testMockedException(new FeedException("error"));
    }

    @Test
    public final void testConnectException() throws IllegalArgumentException, FeedException, IOException,
            FetcherException {
        testMockedException(new ConnectException("error"));
    }

    @Test
    public final void testIOException() throws IllegalArgumentException, FeedException, IOException,
            FetcherException {
        testMockedException(new IOException("error"));
    }

    private final void testMockedException(Exception e) throws IllegalArgumentException, FeedException,
            IOException, FetcherException {
        PortletPreferencesWrapperBeanValidator validator =
                new PortletPreferencesWrapperBeanValidator(feedFetcherMock);
        PortletPreferencesWrapperBean portletPreferencesWrapperBean = new PortletPreferencesWrapperBean();
        BindingResult bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");

        given(feedFetcherMock.retrieveFeed(any(URL.class))).willThrow(e);

        portletPreferencesWrapperBean.setRssFeedLink1("http://vgregion.se");
        validator.validate(portletPreferencesWrapperBean, bindingResult);

        assertEquals(1, bindingResult.getErrorCount());
    }

    @Test
    public final void testFeedOK() throws IllegalArgumentException, FeedException, IOException,
            FetcherException {
        PortletPreferencesWrapperBeanValidator validator =
                new PortletPreferencesWrapperBeanValidator(feedFetcherMock);
        PortletPreferencesWrapperBean portletPreferencesWrapperBean = new PortletPreferencesWrapperBean();

        BindingResult bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");

        bindingResult = new BeanPropertyBindingResult(portletPreferencesWrapperBean, "command");
        given(feedFetcherMock.retrieveFeed(any(URL.class))).willReturn(null);
        portletPreferencesWrapperBean.setRssFeedLink1("http://vgregion.se");
        validator.validate(portletPreferencesWrapperBean, bindingResult);
        assertEquals(0, bindingResult.getErrorCount());
    }

    private class MySub extends PortletPreferencesWrapperBean {
        private static final long serialVersionUID = 1L;
    }
}
