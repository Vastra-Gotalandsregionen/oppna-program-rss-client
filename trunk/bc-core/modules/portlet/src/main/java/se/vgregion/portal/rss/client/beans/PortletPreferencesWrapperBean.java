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
 * @author Simon Göransson - Monator Technologies AB
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
    public static final String NUMBER_OF_ITEM_1 = "numberOfItems1";
    public static final String NUMBER_OF_ITEM_2 = "numberOfItems2";
    public static final String NUMBER_OF_ITEM_3 = "numberOfItems3";
    public static final String NUMBER_OF_ITEM_4 = "numberOfItems4";

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
    public static final String RSS_FEED_LINK_1 = "rssFeedLink1";
    public static final String RSS_FEED_LINK_2 = "rssFeedLink2";
    public static final String RSS_FEED_LINK_3 = "rssFeedLink3";
    public static final String RSS_FEED_LINK_4 = "rssFeedLink4";

    /**
     * The key name to the preference for feed titels.
     */
    public static final String RSS_FEED_TITLE_1 = "rssFeedtitle1";
    public static final String RSS_FEED_TITLE_2 = "rssFeedtitle2";
    public static final String RSS_FEED_TITLE_3 = "rssFeedtitle3";
    public static final String RSS_FEED_TITLE_4 = "rssFeedtitle4";

    private String rssFeedLink1;
    private String rssFeedLink2;
    private String rssFeedLink3;
    private String rssFeedLink4;

    private String numberOfItems1 = String.valueOf(DEFAULT_MAX_NUMBER_OF_ITEMS);
    private String numberOfItems2 = String.valueOf(DEFAULT_MAX_NUMBER_OF_ITEMS);
    private String numberOfItems3 = String.valueOf(DEFAULT_MAX_NUMBER_OF_ITEMS);
    private String numberOfItems4 = String.valueOf(DEFAULT_MAX_NUMBER_OF_ITEMS);

    private String rssFeedTitle1;
    private String rssFeedTitle2;
    private String rssFeedTitle3;
    private String rssFeedTitle4;

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

    public String getRssFeedLink1() {
        return rssFeedLink1;
    }

    public void setRssFeedLink1(String rssFeedLink1) {
        this.rssFeedLink1 = rssFeedLink1;
    }

    public String getRssFeedLink2() {
        return rssFeedLink2;
    }

    public void setRssFeedLink2(String rssFeedLink2) {
        this.rssFeedLink2 = rssFeedLink2;
    }

    public String getRssFeedLink3() {
        return rssFeedLink3;
    }

    public void setRssFeedLink3(String rssFeedLink3) {
        this.rssFeedLink3 = rssFeedLink3;
    }

    public String getRssFeedLink4() {
        return rssFeedLink4;
    }

    public void setRssFeedLink4(String rssFeedLink4) {
        this.rssFeedLink4 = rssFeedLink4;
    }

    public String getNumberOfItems1() {
        return numberOfItems1;
    }

    public void setNumberOfItems1(String numberOfItems1) {
        this.numberOfItems1 = numberOfItems1;
    }

    public String getNumberOfItems2() {
        return numberOfItems2;
    }

    public void setNumberOfItems2(String numberOfItems2) {
        this.numberOfItems2 = numberOfItems2;
    }

    public String getNumberOfItems3() {
        return numberOfItems3;
    }

    public void setNumberOfItems3(String numberOfItems3) {
        this.numberOfItems3 = numberOfItems3;
    }

    public String getNumberOfItems4() {
        return numberOfItems4;
    }

    public void setNumberOfItems4(String numberOfItems4) {
        this.numberOfItems4 = numberOfItems4;
    }

    public String getRssFeedTitle1() {
        return rssFeedTitle1;
    }

    public void setRssFeedTitle1(String rssFeedTitle1) {
        this.rssFeedTitle1 = rssFeedTitle1;
    }

    public String getRssFeedTitle2() {
        return rssFeedTitle2;
    }

    public void setRssFeedTitle2(String rssFeedTitle2) {
        this.rssFeedTitle2 = rssFeedTitle2;
    }

    public String getRssFeedTitle3() {
        return rssFeedTitle3;
    }

    public void setRssFeedTitle3(String rssFeedTitle3) {
        this.rssFeedTitle3 = rssFeedTitle3;
    }

    public String getRssFeedTitle4() {
        return rssFeedTitle4;
    }

    public void setRssFeedTitle4(String rssFeedTitle4) {
        this.rssFeedTitle4 = rssFeedTitle4;
    }

    public String getRssStandardClientPortletLink() {
        return rssStandardClientPortletLink;
    }

    public void setRssStandardClientPortletLink(String rssStandardClientPortletLink) {
        this.rssStandardClientPortletLink = rssStandardClientPortletLink;
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
    public void store(PortletPreferences preferences) throws ReadOnlyException, ValidatorException,
            IOException {
        parseAndFixUrls();
        
        System.out.println("PortletPreferencesWrapperBean - store.");

        preferences.setValue(NUMBER_OF_ITEM_1, numberOfItems1);
        preferences.setValue(NUMBER_OF_ITEM_2, numberOfItems2);
        preferences.setValue(NUMBER_OF_ITEM_3, numberOfItems3);
        preferences.setValue(NUMBER_OF_ITEM_4, numberOfItems4);
        preferences.setValue(RSS_FEED_LINK_1, rssFeedLink1);
        preferences.setValue(RSS_FEED_LINK_2, rssFeedLink2);
        preferences.setValue(RSS_FEED_LINK_3, rssFeedLink3);
        preferences.setValue(RSS_FEED_LINK_4, rssFeedLink4);
        preferences.setValue(RSS_FEED_TITLE_1, rssFeedTitle1);
        preferences.setValue(RSS_FEED_TITLE_2, rssFeedTitle2);
        preferences.setValue(RSS_FEED_TITLE_3, rssFeedTitle3);
        preferences.setValue(RSS_FEED_TITLE_4, rssFeedTitle4);

        preferences.setValue(RSS_STD_CLIENT_LINK, rssStandardClientPortletLink);
        preferences.store();
    }

    private void parseAndFixUrls() {
        if (!StringUtils.isBlank(rssFeedLink1)) {
            rssFeedLink1 = rssFeedLink1.trim().replace(" ", "\n");
        }
        if (!StringUtils.isBlank(rssFeedLink2)) {
            rssFeedLink2 = rssFeedLink2.trim().replace(" ", "\n");
        }
        if (!StringUtils.isBlank(rssFeedLink3)) {
            rssFeedLink3 = rssFeedLink3.trim().replace(" ", "\n");
        }
        if (!StringUtils.isBlank(rssFeedLink4)) {
            rssFeedLink4 = rssFeedLink4.trim().replace(" ", "\n");
        }
    }

}
