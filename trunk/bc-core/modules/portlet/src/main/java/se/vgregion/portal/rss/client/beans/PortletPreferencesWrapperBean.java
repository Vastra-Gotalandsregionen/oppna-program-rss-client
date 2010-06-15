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
package se.vgregion.portal.rss.client.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.apache.commons.lang.StringUtils;

/**
 * @author Anders Asplund - Callista Enterprise
 * @author Jonas Liljenfeldt - Know IT
 * 
 */
public class PortletPreferencesWrapperBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 20;
    public static final String NUMBER_OF_ITEMS = "numberOfItems";
    public static final String RSS_FEED_LINKS = "rssFeedLinks";
    public String rssFeedLinks;
    public String numberOfItems;

    public void setRssFeedLinks(String rssFeedLinks) throws ReadOnlyException {
        this.rssFeedLinks = rssFeedLinks;
    }

    public void setNumberOfItems(String numberOfItems) throws ReadOnlyException {
        this.numberOfItems = numberOfItems;
    }

    public String getRssFeedLinks() {
        return rssFeedLinks;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public void store(PortletPreferences preferences) throws ValidatorException, IOException, ReadOnlyException {
        parseAndFixUrls();

        preferences.setValue(NUMBER_OF_ITEMS, numberOfItems);
        preferences.setValue(RSS_FEED_LINKS, rssFeedLinks);
        preferences.store();
    }

    private void parseAndFixUrls() {
        if (!StringUtils.isBlank(rssFeedLinks)) {
            rssFeedLinks = rssFeedLinks.replace(" ", "\n");
        }
    }
}
