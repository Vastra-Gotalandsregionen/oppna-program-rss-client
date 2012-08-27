package se.vgregion.portal.rss.blacklist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rometools.fetcher.FeedFetcher;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.*;


public class FeedBlackListCleanerTest {
    private static final Set<String> BLACKED_LISTED_ITEMS = new HashSet<String>(Arrays.asList("http://example.com"));
    @Mock private BlackList<String> blackList;
    @Mock private FeedFetcher feedFetcher;
    private FeedBlackListCleaner cleaner;

    @Before
    public final void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        cleaner = new FeedBlackListCleaner(feedFetcher, blackList);
        given(blackList.items()).willReturn(BLACKED_LISTED_ITEMS);
    }

    @Test
    public final void shouldRemoveItemFromBlackList() throws Exception {
        given(feedFetcher.retrieveFeed(any(URL.class))).willReturn(null);

        //When
        cleaner.run();

        verify(blackList, times(1)).remove(anyString());
    }

    @Test
    public final void shouldKeepItemOnBlackListOnConnectException() throws Exception {
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new ConnectException());

        //When
        cleaner.run();

        verify(blackList, times(0)).remove(anyString());
    }

    @Test
    public final void shouldRemoveItemFromBlackListOnOtherExceptions() throws Exception {
        given(feedFetcher.retrieveFeed(any(URL.class))).willThrow(new MalformedURLException());

        //When
        cleaner.run();

        verify(blackList, times(1)).remove(anyString());
    }
}
