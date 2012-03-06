package se.vgregion.portal.rss.blacklist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConcurrentBlackListTest {

    private ConcurrentBlackList<String> blackList;
    private static final String FEED_URL = "http://example.com";

    @Before
    public void setup() throws Exception {
        blackList = new ConcurrentBlackList<String>();
    }

    @Test
    public final void shouldAddNewItemToBlackList() throws Exception {
        blackList.add(FEED_URL);
        assertTrue(blackList.contains(FEED_URL));
    }

    @Test
    public final void shouldRemoveItemFromBlackList() throws Exception {
        blackList.add(FEED_URL);
        assertTrue(blackList.contains(FEED_URL));
        blackList.remove(FEED_URL);
        assertFalse(blackList.contains(FEED_URL));
    }

    @Test
    public final void shouldGetItmesInBlackList() throws Exception {
        blackList.add(FEED_URL);
        blackList.add(FEED_URL + "/1");
        blackList.add(FEED_URL + "/2");
        assertEquals(3, blackList.items().size());
    }

    @Test
    public final void shouldCheckForExistanceInBlackList() throws Exception {
        blackList.add(FEED_URL);
        assertTrue(blackList.contains(FEED_URL));
        assertFalse(blackList.contains(FEED_URL + "/1"));
    }

}
