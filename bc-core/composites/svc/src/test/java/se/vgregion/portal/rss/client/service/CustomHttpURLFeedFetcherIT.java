package se.vgregion.portal.rss.client.service;

import com.sun.syndication.feed.synd.SyndFeed;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static void main2(String[] args) throws ParserConfigurationException, IOException, SAXException {
        URL fXmlFile = new URL("https://regionkalender.vgregion.se/regionkalender/main.nsf/atom?openagent&days=365&show=2&orgs=%20AND%20((%5Borg1%5D~o1))");
        //File fXmlFile = new File("/Users/mkyong/staff.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputStream is = fXmlFile.openStream();
        Document doc = dBuilder.parse(is);
        Element root = doc.getDocumentElement();
        NodeList entries = root.getElementsByTagName("entry");
        for (int i = 0, j = entries.getLength(); i < j; i++) {
            Node item = entries.item(i);
            System.out.println(item);
            NodeList children = item.getChildNodes();
            for (int k = 0, l = children.getLength(); k < l; k++) {
                Node child = children.item(k);
                System.out.println(child.getNodeName() + " = " + child.getTextContent());

            }
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        URL fXmlFile = new URL("https://regionkalender.vgregion.se/regionkalender/main.nsf/atom?openagent&days=365&show=2&orgs=%20AND%20((%5Borg1%5D~o1))");
        //File fXmlFile = new File("/Users/mkyong/staff.xml");

        InputStream is = fXmlFile.openStream();
        main(is);
    }

    public static List<Map<String, String>> main(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
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

    public static Object toMapTree(Node node) {
        Map<String, Object> map = new HashMap<String, Object>();
        NodeList children = node.getChildNodes();
        int cl = children.getLength();
        if (cl == 0) {

        } else {
            return node.getTextContent();
        }
        return map;
    }

    class ListsMap<K, V> extends HashMap<K, List<V>> {
        @Override
        public List<V> get(Object key) {
            List<V> result = super.get(key);
            if (result == null) {
                result = new ArrayList<V>();
                put((K) key, result);
            }
            return result;
        }
    }

}
