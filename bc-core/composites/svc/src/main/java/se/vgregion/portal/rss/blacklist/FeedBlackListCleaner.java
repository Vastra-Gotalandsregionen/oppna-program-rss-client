package se.vgregion.portal.rss.blacklist;

import java.net.ConnectException;
import java.net.URL;
import java.util.TimerTask;

import org.rometools.fetcher.FeedFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BlackListCleanup is a {@link TimerTask} which can be scheduled to run on repeated intervals. Its purpose is to
 * go through a {@link BlackList} with feed url:s and control if the url can be "whitelisted" again.
 * 
 * @author Anders Asplund
 * 
 */
public class FeedBlackListCleaner extends TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(FeedBlackListCleaner.class);

    private BlackList<String> blackList;
    private final FeedFetcher feedFetcher;

    /**
     * Constructs an instance of the {@link TimerTask} BlackListCleanup.

     * @param feedFetcher the feedFetcher
     * @param blackList the blackList
     */
    public FeedBlackListCleaner(FeedFetcher feedFetcher, BlackList<String> blackList) {
        this.feedFetcher = feedFetcher;
        this.blackList = blackList;
    }

    @Override
    public void run() {
        for (String feedLink : blackList.items()) {
            try {
                feedFetcher.retrieveFeed(new URL(feedLink));
                blackList.remove(feedLink);
                LOG.info("Remove {} from BlackList", feedLink);
            } catch (ConnectException e) {
                LOG.info("{} will stay on BlackList", feedLink);
            } catch (Exception e) {
                blackList.remove(feedLink);
                LOG.info("Remove {} from BlackList", feedLink);
            }
        }
    }
}
