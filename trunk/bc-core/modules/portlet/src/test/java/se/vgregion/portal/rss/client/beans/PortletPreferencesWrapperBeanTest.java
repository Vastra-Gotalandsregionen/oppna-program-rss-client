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

import static org.junit.Assert.*;

import java.io.IOException;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockPortletPreferences;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class PortletPreferencesWrapperBeanTest {

    PortletPreferencesWrapperBean portletPreferencesWrapperBean;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        portletPreferencesWrapperBean = new PortletPreferencesWrapperBean();

        portletPreferencesWrapperBean.setNumberOfItems("10");
        portletPreferencesWrapperBean.setRssFeedLinks("link1 link2");
        portletPreferencesWrapperBean.setNumberOfExcerptRows("1");
        portletPreferencesWrapperBean.setRssStandardClientPortletLink("hejsan");
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean#store(javax.portlet.PortletPreferences)}
     * .
     * 
     * @throws IOException
     * @throws ValidatorException
     * @throws ReadOnlyException
     */
    @Test
    public final void testStore() throws ReadOnlyException, ValidatorException, IOException {
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();

        portletPreferencesWrapperBean.store(mockPortletPreferences);

        assertEquals("10", mockPortletPreferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS, ""));
        assertEquals("link1\nlink2",
                mockPortletPreferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, ""));
        assertEquals("1",
                mockPortletPreferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_EXCERPT_ROWS, ""));
        assertEquals("hejsan",
                mockPortletPreferences.getValue(PortletPreferencesWrapperBean.RSS_STD_CLIENT_LINK, ""));
    }

    @Test
    public final void testTrimTrailingSpacesOnFeedLinks() throws ReadOnlyException, ValidatorException,
            IOException {
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();

        portletPreferencesWrapperBean.setRssFeedLinks("link1 link2  ");

        portletPreferencesWrapperBean.store(mockPortletPreferences);

        assertEquals("link1\nlink2",
                mockPortletPreferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, ""));
    }
}
