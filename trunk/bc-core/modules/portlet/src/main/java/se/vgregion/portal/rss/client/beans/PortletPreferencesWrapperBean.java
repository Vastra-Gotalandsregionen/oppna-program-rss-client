/**
 * 
 */
package se.vgregion.portal.rss.client.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

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
    private String rssFeedLinks = "";
    private String numberOfItems;

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
        preferences.setValue(NUMBER_OF_ITEMS, numberOfItems);
        preferences.setValue(RSS_FEED_LINKS, rssFeedLinks);
        preferences.store();
    }
}
