package se.vgregion.portal.rss.client.service;

import com.liferay.portal.kernel.io.ByteArrayFileInputStream;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.apache.commons.io.IOUtils;
import org.rometools.fetcher.FetcherEvent;
import org.rometools.fetcher.FetcherListener;
import org.rometools.fetcher.impl.HttpURLFeedFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.vgregion.portal.rss.util.XmlTransformationTool;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class overrides one method of {@link HttpURLFeedFetcher} in order to work around the fact that dates often are
 * given in a wrong format, i.e. in a locale which does not conform to the RFC822 standard
 * (http://www.w3.org/Protocols/rfc822/).
 *
 * @author Patrik Bergström
 */
public class CustomHttpURLFeedFetcher extends HttpURLFeedFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpURLFeedFetcher.class);
    private SyndFeedInput syndFeedInput = new SyndFeedInput();

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
        LOGGER.debug("Retrieve feed: " + feedUrl.toString());

        InputStream modifiedStream = null;
        try {
            // Possibly modify the response to correct dates
            modifiedStream = getModifiedStream(feedUrl);

            byte[] bytes = IOUtils.toByteArray(modifiedStream);

            //ByteArrayOutputStream copiedStream = new ByteArrayOutputStream();
            //IOUtils.copy(modifiedStream, copiedStream);

            Reader r = new InputStreamReader(new ByteArrayInputStream(bytes));
            SyndFeed syndFeed = syndFeedInput.build(r);

            List<Map<String, String>> maps = toMaps(new ByteArrayInputStream(bytes));
            allInformationFromLatestFeed.set(maps);

            /*int i = 0;
            for (Object item : syndFeed.getEntries()) {
                SyndEntry se = (SyndEntry) item;
                Object foreignMarkup = se.getForeignMarkup();
                System.out.println(foreignMarkup);
                se.setForeignMarkup(maps.get(i));
                i++;
            }*/

            LOGGER.debug("Retrieved " + syndFeed.getEntries().size() + " entries from " + feedUrl.toString());
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

    private final static ThreadLocal<List<Map<String, String>>> allInformationFromLatestFeed = new ThreadLocal<List<Map<String, String>>>();

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


    public static List<Map<String, String>> toMaps(ByteArrayInputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // InputStream is = new ByteArrayInputStream(baos.toByteArray());

        Document doc = dBuilder.parse(is);
        Element root = doc.getDocumentElement();
        NodeList entries = root.getElementsByTagName("entry");
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (int i = 0, j = entries.getLength(); i < j; i++) {
            Map<String, String> map = new HashMap<String, String>();
            result.add(map);
            Node item = entries.item(i);
            System.out.println(item);
            NodeList children = item.getChildNodes();
            for (int k = 0, l = children.getLength(); k < l; k++) {
                Node child = children.item(k);
                // System.out.println(child.getNodeName() + " = " + child.getTextContent());
                map.put(child.getNodeName(), child.getTextContent());
            }
        }
        return result;
    }

    public ThreadLocal<List<Map<String, String>>> getAllInformationFromLatestFeed() {
        return allInformationFromLatestFeed;
    }
}
