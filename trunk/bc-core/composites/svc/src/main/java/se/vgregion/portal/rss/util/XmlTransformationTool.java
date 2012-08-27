package se.vgregion.portal.rss.util;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class for transforming XML.
 *
 * @author Patrik Bergström
 */
public final class XmlTransformationTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlTransformationTool.class);

    static final SimpleDateFormat SWEDISH_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
            new Locale("sv", "SE"));
    static final SimpleDateFormat ENGLISH_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

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
                    List pubDates = e.getChild("pubDate").getContent();
                    for (Object pubDate : pubDates) {
                        Text dateText = (Text) pubDate;
                        String swedishDate = dateText.getText();
                        String englishDate = toEnglishDate(swedishDate);
                        dateText.setText(englishDate);
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
        Date date = null;
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
                LOGGER.warn("Couldn't parse \"" + swedishDate + "\" in Swedish nor English format.");
                LOGGER.warn(e.getMessage(), e);
                // Not English either. Return empty string.
                return "";
            }
        }

        return null;
    }
}
