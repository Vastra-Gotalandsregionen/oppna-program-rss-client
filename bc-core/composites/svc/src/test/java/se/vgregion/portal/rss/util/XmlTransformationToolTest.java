package se.vgregion.portal.rss.util;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * @author Patrik Bergström
 */
public class XmlTransformationToolTest {

    @Test
    public void testRssTransformDatesToEnglish() throws FeedException, IOException {

        // When we transform
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("rss-feed.xml");
        InputStream inputStream = XmlTransformationTool.transformDatesToEnglish(resourceAsStream);

        // Verify that we still can parse
        verifyDates(resourceAsStream, inputStream);

        assertNotSame(resourceAsStream, inputStream);
    }

    @Test
    public void testRssWithAlreadyEnglishDatesTransformDatesToEnglish() throws FeedException, IOException {

        // When we transform
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("rss-feed-already-english-dates.xml");
        InputStream inputStream = XmlTransformationTool.transformDatesToEnglish(resourceAsStream);

        // Verify that we still can parse
        verifyDates(resourceAsStream, inputStream);
    }

    @Test
    public void testAtomTransformDatesToEnglish() throws FeedException, IOException {

        // When we transform
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("atom-feed.xml");
        InputStream inputStream = XmlTransformationTool.transformDatesToEnglish(resourceAsStream);

        // Verify that we still can parse
        verifyDates(resourceAsStream, inputStream);
    }

    @Test
    public void testToEnglishDateWithEnglishDate() {
        String englishDate = "Fri, 14 May 2010 13:22:11 GMT";

        String date = XmlTransformationTool.toEnglishDate(englishDate);

        assertEquals(englishDate, date);
    }

    @Test
    public void testToEnglishDateWithSwedishDate() throws ParseException {

        // Given
        String englishDate = "Fri, 14 May 2010 13:22:11 GMT";
        String swedishDate = "fr, 14 maj 2010 13:22:11 GMT";
        Date originalDate = XmlTransformationTool.ENGLISH_FORMAT.parse(englishDate);

        // When
        String resultDate = XmlTransformationTool.toEnglishDate(swedishDate);

        // Then
        // First verify we have the correct locale
        Date afterConversionDate = XmlTransformationTool.ENGLISH_FORMAT.parse(resultDate);
        // No exception means successful parse.

        // Then verify we have the correct point in time
        assertEquals(originalDate, afterConversionDate);

        // Last, verify correct timezone
        SimpleDateFormat englishFormat = (SimpleDateFormat) XmlTransformationTool.ENGLISH_FORMAT.clone();
        englishFormat.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
        String englishWithCorrectTimeZoone = englishFormat.format(afterConversionDate);
        assertEquals(englishWithCorrectTimeZoone, resultDate); //"Fri, 14 May 2010 15:22:11 CEST" or "Fri, 14 May 2010 14:22:11 CET"
    }

    @SuppressWarnings("unchecked")
    private void verifyDates(InputStream resourceAsStream, InputStream inputStream) throws FeedException, IOException {
        SyndFeed build = new SyndFeedInput().build(new InputStreamReader(inputStream));

        List<SyndEntry> entries = build.getEntries();

        resourceAsStream.close();

        for (SyndEntry entry : entries) {
            assertTrue(entry.getUpdatedDate() != null || entry.getPublishedDate() != null);
        }
    }
}
