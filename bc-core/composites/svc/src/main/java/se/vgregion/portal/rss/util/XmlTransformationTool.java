package se.vgregion.portal.rss.util;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class for transforming XML.
 *
 * @author Patrik Bergström
 */
public final class XmlTransformationTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlTransformationTool.class);

    // To be backwards compatible independently of what the system property java.locale.providers is set to.
    static String[] newShortMonths = {"jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec", ""};
    static String[] newShortWeekdays = {"", "sö", "må", "ti", "on", "to", "fr", "lö"};

    static final SimpleDateFormat SWEDISH_FORMAT;

    static {
        DateFormatSymbols dateFormatSymbols = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
                new Locale("sv", "SE")).getDateFormatSymbols();

        dateFormatSymbols.setShortWeekdays(newShortWeekdays);
        dateFormatSymbols.setShortMonths(newShortMonths);

        SWEDISH_FORMAT = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", dateFormatSymbols);
    }

    static final SimpleDateFormat ENGLISH_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    static final List<DateTimeFormatter> OTHER_POSSIBLE_FORMATS = Arrays.asList(
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mmZ"),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    );

    static {
        SWEDISH_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm")); // Explicitly set timezone.
        ENGLISH_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm")); // Explicitly set timezone.
    }

    private XmlTransformationTool() {

    }

    /**
     * This method takes in {@link InputStream} and returns another {@link InputStream} where potential dates in Swedish
     * locale have been transformed to English locale. If the input already contains dates in English format they are
     * kept as is. If the dates cannot be parsed in neither format they will be set as empty strings.
     *
     * @param input the input
     * @return the modified {@link InputStream}
     */
    public static InputStream transformDatesToEnglish(InputStream input) {

        try {
            SAXBuilder builder = new SAXBuilder();

            Document document = builder.build(input);

            Element channel = document.getRootElement().getChild("channel");

            if (channel != null) {
                // Means we are dealing with an RSS feed
                List item = channel.getChildren("item");
                for (Object element : item) {
                    Element e = (Element) element;
                    Element pubDateElement = e.getChild("pubDate");
                    if (pubDateElement != null) {
                        List pubDates = pubDateElement.getContent();
                        for (Object pubDate : pubDates) {
                            Text dateText = (Text) pubDate;
                            String swedishDate = dateText.getText();
                            String englishDate = toEnglishDate(swedishDate);
                            dateText.setText(englishDate);
                        }
                    } else {
                        // Try another rss format.
                        pubDateElement = e.getChild("date", Namespace.getNamespace("http://purl.org/dc/elements/1.1/"));
                        if (pubDateElement != null) {
                            List pubDates = pubDateElement.getContent();
                            for (Object pubDate : pubDates) {
                                Text dateText = (Text) pubDate;
                                String swedishDate = dateText.getText();
                                String englishDate = toEnglishDate(swedishDate);
                                dateText.setText(englishDate);
                            }
                        }
                    }
                }
            }

            XMLOutputter out = new XMLOutputter();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            out.output(document, byteArrayOutputStream);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            return byteArrayInputStream;

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (JDOMException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    static String toEnglishDate(String swedishDate) {
        Date date;
        try {
            date = SWEDISH_FORMAT.parse(swedishDate);
            String englishDate = ENGLISH_FORMAT.format(date);
            return englishDate;
        } catch (ParseException e) {
            // We might already have a correct date in English so let's test that
            try {
                date = ENGLISH_FORMAT.parse(swedishDate);
                if (date != null) {
                    // Successfully parsed. We had an English date from the beginning.
                    String englishDate = swedishDate;
                    return englishDate;
                }
            } catch (ParseException e1) {
                // Now try the "other formats" with Joda Time which was needed since the SimpleDateFormat couldn't
                // handle timezones with ":" like "2011-11-30T10:49:33+01:00". On the other hand time Joda Time can't
                // parse "z" strings like "GMT" so therefore the SimpleDateFormat is kept too.
                for (DateTimeFormatter format : OTHER_POSSIBLE_FORMATS) {
                    try {
                        DateTime dateTime = format.parseDateTime(swedishDate);
                        // No exception means we could parse it.
                        String englishDate = ENGLISH_FORMAT.format(dateTime.toDate());
                        return englishDate;
                    } catch (IllegalArgumentException e2) {

                    }
                }

                // Failover with other short week day names.
                DateFormatSymbols dateFormatSymbols = SWEDISH_FORMAT.getDateFormatSymbols();
                String[] originalShortWeekdays = dateFormatSymbols.getShortWeekdays();
                if (originalShortWeekdays[1].length() == 3) {
                    String[] newShortWeekdays = {"", "sö", "må", "ti", "on", "to", "fr", "lö"};
                    dateFormatSymbols.setShortWeekdays(newShortWeekdays);
                    SWEDISH_FORMAT.setDateFormatSymbols(dateFormatSymbols);
                } else if (originalShortWeekdays[1].length() == 2) {
                    String[] newShortWeekdays = {"", "sön", "mån", "tis", "ons", "tor", "fre", "lör"};
                    dateFormatSymbols.setShortWeekdays(newShortWeekdays);
                    SWEDISH_FORMAT.setDateFormatSymbols(dateFormatSymbols);
                }

                try {
                    Date dateTime = SWEDISH_FORMAT.parse(swedishDate);
                    // No exception means we could parse it.
                    String englishDate = ENGLISH_FORMAT.format(dateTime);
                    return englishDate;
                } catch (ParseException e2) {}

                LOGGER.warn("Couldn't parse \"" + swedishDate + "\" in any format.");
                // No format worked. Return empty string.
                return "";
            }
        }

        return null;
    }
}
