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
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.apache.commons.lang.StringUtils;

/**
 * Container for the portlet preferences.
 * 
 * @author Anders Asplund - Callista Enterprise
 * @author Jonas Liljenfeldt - Know IT
 */
public class PortletPreferencesWrapperBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Max no of items that can be shown, whatever the user may think.
     */
    public static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 20;

    /**
     * The key name to the preference number of items to display.
     */
    public static final String NUMBER_OF_ITEMS = "numberOfItems";

    /**
     * Default number of excerpt rows.
     */
    public static final int DEFAULT_NUMBER_OF_EXCERPT_ROWS = 1;

    /**
     * The key name to the preference number of rows to display in excerpt.
     */
    public static final String NUMBER_OF_EXCERPT_ROWS = "numberOfExcerptRows";

    /**
     * The key name to the preference number of rows to display in excerpt.
     */
    public static final String RSS_STD_CLIENT_LINK = "rssStandardClientPortletLink";

    /**
     * The key name to the preference for feed urls.
     */
    public static final String RSS_FEED_LINKS = "rssFeedLinks";
    private String rssFeedLinks;
    private String numberOfItems = String.valueOf(DEFAULT_MAX_NUMBER_OF_ITEMS);
    private String numberOfExcerptRows = String.valueOf(DEFAULT_NUMBER_OF_EXCERPT_ROWS);
    private String rssStandardClientPortletLink;
    private List<String> filteredBlackList;

    /**
     * @return the feedBlackList
     */
    public List<String> getFeedBlackList() {
        return filteredBlackList;
    }

    /**
     * @param feedBlackList
     *            the feedBlackList to set
     */
    public void setFeedBlackList(List<String> filteredBlackList) {
        this.filteredBlackList = filteredBlackList;
    }

    /**
     * Sets the Rss Feed urls. The urls are sparated by comma(,) or new line.
     * 
     * @param rssFeedLinks
     *            the urls
     */
    public void setRssFeedLinks(String rssFeedLinks) {
        this.rssFeedLinks = rssFeedLinks;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void setNumberOfExcerptRows(String numberOfExcerptRows) {
        this.numberOfExcerptRows = numberOfExcerptRows;
    }

    public void setRssStandardClientPortletLink(String rssStandardClientLink) {
        this.rssStandardClientPortletLink = rssStandardClientLink;
    }

    public String getRssFeedLinks() {
        return rssFeedLinks;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public String getNumberOfExcerptRows() {
        return numberOfExcerptRows;
    }

    public String getRssStandardClientPortletLink() {
        return rssStandardClientPortletLink;
    }

    /**
     * Stores this bean values in the provided PortletPreferences.
     * 
     * @param preferences
     *            the PortletPreference to store the bean values
     * @throws ReadOnlyException
     *             Thrown if the portlet container is unable write the bean values in the PortletPreferences
     * @throws IOException
     *             Thrown if the portlet container is unable store the PortletPreferences
     * @throws ValidatorException
     *             Thrown if the portlet container is unable store the PortletPreferences
     */
    public void store(PortletPreferences preferences) throws ReadOnlyException, ValidatorException, IOException {
        parseAndFixUrls();

        preferences.setValue(NUMBER_OF_ITEMS, numberOfItems);
        preferences.setValue(RSS_FEED_LINKS, rssFeedLinks);
        preferences.setValue(NUMBER_OF_EXCERPT_ROWS, numberOfExcerptRows);
        preferences.setValue(RSS_STD_CLIENT_LINK, rssStandardClientPortletLink);
        preferences.store();
    }

    private void parseAndFixUrls() {
        if (!StringUtils.isBlank(rssFeedLinks)) {
            rssFeedLinks = rssFeedLinks.trim().replace(" ", "\n");
        }
    }
}
