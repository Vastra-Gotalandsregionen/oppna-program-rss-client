package se.vgregion.portal.rss.client.service;

import com.sun.syndication.feed.synd.SyndFeed;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
public class CustomHttpURLFeedFetcherIT {

    private CustomHttpURLFeedFetcher customHttpURLFeedFetcher = new CustomHttpURLFeedFetcher();

    @Test
    @Ignore
    public void testRetrieveFeedAtom() throws Exception {
        SyndFeed syndFeed = customHttpURLFeedFetcher.retrieveFeed(new URL(
                "http://hitta.vgregion.se/feed/default/all/search.atom?type=feed&q=regionstyrelsen&1facet_infotype=p-facet.infotype.news"));

        assertTrue(syndFeed.getEntries().size() > 0);
    }

    @Test
    @Ignore
    public void testRetrieveFeedRss() throws Exception {
        SyndFeed syndFeed = customHttpURLFeedFetcher.retrieveFeed(new URL(
                "http://hitta.vgregion.se/feed/default/all/search.rss?type=feed&q=regionstyrelsen&"
                        + "1facet_infotype=p-facet.infotype.news"));

        assertTrue(syndFeed.getEntries().size() > 0);
    }

    @Test
    @Ignore
    public void testRetrieveFeedRss2() throws Exception {
        SyndFeed syndFeed = customHttpURLFeedFetcher.retrieveFeed(new URL(
                "http://ifeed.vgregion.se/iFeed-intsvc/documentlists/3686479/feed?by=dc.title&dir=desc"));

        assertTrue(syndFeed.getEntries().size() > 0);
    }

    // Test multi-threading.
    @Test
    @Ignore
    public void testRetrieveFeedRss3() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<SyndFeed>> list = new ArrayList<Future<SyndFeed>>();
        for (int i = 0; i < 8; i++) {

            Future<SyndFeed> submit = service.submit(new Callable<SyndFeed>() {
                @Override
                public SyndFeed call() throws Exception {
                    return customHttpURLFeedFetcher.retrieveFeed(new URL(
                            "http://ifeed.vgregion.se/iFeed-intsvc/documentlists/3686484/feed?by=dc.title&dir=desc&a=a"));
                }
            });

            list.add(submit);
        }

        for (Future<SyndFeed> syndFeedFuture : list) {
            assertTrue(syndFeedFuture.get().getEntries().size() > 0);

        }
    }
}
