package se.vgregion.portal.rss.client.service;

import com.sun.syndication.feed.synd.SyndFeed;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
public class CustomHttpURLFeedFetcherIT {

    private CustomHttpURLFeedFetcher customHttpURLFeedFetcher = new CustomHttpURLFeedFetcher();

    @Test
    public void testRetrieveFeedAtom() throws Exception {
        SyndFeed syndFeed = customHttpURLFeedFetcher.retrieveFeed(new URL(
                "http://hitta.vgregion.se/feed/default/all/search.atom?type=feed&q=regionstyrelsen&1facet_infotype=p-facet.infotype.news"));

        assertTrue(syndFeed.getEntries().size() > 0);
    }

    @Test
    public void testRetrieveFeedRss() throws Exception {
        SyndFeed syndFeed = customHttpURLFeedFetcher.retrieveFeed(new URL(
                "http://hitta.vgregion.se/feed/default/all/search.rss?type=feed&q=regionstyrelsen&"
                        + "1facet_infotype=p-facet.infotype.news"));

        assertTrue(syndFeed.getEntries().size() > 0);
    }
}
