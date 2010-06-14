package se.vgregion.portal.rss.client.beans;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component
public class PortletPreferencesWrapperBeanValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PortletPreferencesWrapperBean.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PortletPreferencesWrapperBean bean = (PortletPreferencesWrapperBean) target;
        
        if (bean.getNumberOfItems() != null && !bean.getNumberOfItems().matches("^\\d+")) {
            errors.rejectValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS, "invalidnoofitems", "Antalet inlägg måste vara större än noll");
        }
        
        SyndFeedInput syndFeedInput = new SyndFeedInput();
        URL feedUrl = null;
        if (!StringUtils.isBlank(bean.getRssFeedLinks())) {
            for (String url : bean.getRssFeedLinks().split("\n")) {
                try {
                    feedUrl = new URL(url);
                    syndFeedInput.build(new XmlReader(feedUrl));
                } catch (MalformedURLException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl", new Object[] {url}, 
                            "Ogiltig adress");
                } catch (IllegalArgumentException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl", new Object[] {url}, 
                            "Ogiltig adress");
                } catch (ParsingFeedException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidrssurl", new Object[] {url}, 
                            "Ogiltig adress");
                } catch (FeedException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidrssurl", new Object[] {url}, 
                            "Ogiltig adress");
                } catch (IOException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl", new Object[] {url}, 
                            "Ogiltig adress");
                }
            }
        }
    }
}