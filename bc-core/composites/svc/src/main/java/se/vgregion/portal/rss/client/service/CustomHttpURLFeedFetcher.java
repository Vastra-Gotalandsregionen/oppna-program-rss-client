package se.vgregion.portal.rss.client.service;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.rometools.fetcher.impl.HttpURLFeedFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.rss.util.XmlTransformationTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class overrides one method of {@link HttpURLFeedFetcher} in order to work around the fact that dates often are
 * given in a wrong format, i.e. in a locale which does not conform to the RFC822 standard
 * (http://www.w3.org/Protocols/rfc822/).
 *
 * @author Patrik Bergström
 */
public class CustomHttpURLFeedFetcher extends HttpURLFeedFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpURLFeedFetcher.class);

    /**
     * This method retrieves the {@link SyndFeed} from a {@link URL}. The difference between
     * {@link org.rometools.fetcher.impl.HttpURLFeedFetcher#retrieveFeed(java.net.URL)} and this method is that this
     * method modifies the returned XML to make the dates conform the the RFC822 standard by transforming dates in
     * Swedish format to English format.
     *
     * @param feedUrl the URL to retrieve feeds from
     * @return the resulting feed
     * @throws IOException   IOException
     * @throws FeedException FeedException
     */
    @Override
    public SyndFeed retrieveFeed(URL feedUrl) throws IOException, FeedException {
        InputStream modifiedStream = null;
        try {
            // Possibly modify the response to correct dates
            modifiedStream = getModifiedStream(feedUrl);

            XmlReader reader = new XmlReader(modifiedStream);
            SyndFeed syndFeed = new SyndFeedInput().build(reader);
            return syndFeed;
        } catch (Exception e) {
            LOGGER.error("Feed: " + feedUrl.toString() + " - " + e.getMessage(), e);
            return null;
        } finally {
            if (modifiedStream != null) {
                modifiedStream.close();
            }
        }
    }

    // In case the dates are in Swedish we need to transform them to English to conform to the standard.
    protected InputStream getModifiedStream(URL feedUrl) throws IOException {
        InputStream urlInputStream = null;
        URLConnection urlConnection = null;
        try {
            urlConnection = feedUrl.openConnection();
            urlInputStream = urlConnection.getInputStream();
            return XmlTransformationTool.transformDatesToEnglish(urlInputStream);
        } finally {
            if (urlInputStream != null) {
                urlInputStream.close();
            }
        }
    }
}
