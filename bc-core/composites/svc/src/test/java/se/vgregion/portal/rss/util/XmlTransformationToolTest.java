package se.vgregion.portal.rss.util;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    public void testParseDates() throws ParseException {
        SimpleDateFormat SWEDISH_FORMAT_FOR_DATEFORMAT_SYMBOLS = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
                new Locale("sv", "SE"));

        SimpleDateFormat SWEDISH_FORMAT = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z",
                SWEDISH_FORMAT_FOR_DATEFORMAT_SYMBOLS.getDateFormatSymbols());

        DateFormatSymbols dateFormatSymbols = SWEDISH_FORMAT.getDateFormatSymbols();

        String[] newShortWeekdays = {"", "sö", "må", "ti", "on", "to", "fr", "lö"};
        String[] newShortMonths = {"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec", ""};
        dateFormatSymbols.setShortWeekdays(newShortWeekdays);
        dateFormatSymbols.setShortMonths(newShortMonths);
        SWEDISH_FORMAT.setDateFormatSymbols(dateFormatSymbols);

        SWEDISH_FORMAT.parse("on, 06 dec 2008 00:00:00 CEST");
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
        String swedishDate = "fre, 14 maj 2010 13:22:11 GMT";
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
        String englishWithCorrectTimeZone = englishFormat.format(afterConversionDate);
        assertEquals(englishWithCorrectTimeZone, resultDate); //"Fri, 14 May 2010 15:22:11 CEST" or "Fri, 14 May 2010 14:22:11 CET"
    }

    @Test
    public void testDateFormats() {
        String[] dateStrings = {
                "2011-11-30T10:49+01:00",
                "2011-11-30T10:49:59+01:00",
                "2011-11-30T10:49+0100",
                "2011-11-30T10:49:59+0100"
        };

        for (String dateString : dateStrings) {

            Date date = null;
            for (DateTimeFormatter formatter : XmlTransformationTool.OTHER_POSSIBLE_FORMATS) {
                try {
                    date = formatter.parseDateTime(dateString).toDate();
                    if (date != null) {
                        break;
                    }
                } catch (IllegalArgumentException e) {
                }
            }

            assertNotNull(date);
        }

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
