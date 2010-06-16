/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return PortletPreferencesWrapperBean.class.equals(clazz);
    }

    /**
     * Validates a {@link PortletPreferencesWrapperBean}.
     * 
     * @param target The {@link PortletPreferencesWrapperBean} to validate
     * @param errors An error object provided by the spring container potentially containing the validation errors.
     */
    @Override
    public void validate(Object target, Errors errors) {
        PortletPreferencesWrapperBean bean = (PortletPreferencesWrapperBean) target;

        if (bean.getNumberOfItems() != null && !bean.getNumberOfItems().matches("^\\d+")) {
            errors.rejectValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS, "invalidnoofitems",
                    "Antalet inlägg måste vara större än noll");
        }

        SyndFeedInput syndFeedInput = new SyndFeedInput();
        URL feedUrl = null;
        if (!StringUtils.isBlank(bean.getRssFeedLinks())) {
            for (String url : bean.getRssFeedLinks().split("\n")) {
                try {
                    feedUrl = new URL(url);
                    syndFeedInput.build(new XmlReader(feedUrl));
                } catch (MalformedURLException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl",
                            new Object[] {url}, "Ogiltig adress");
                } catch (IllegalArgumentException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl",
                            new Object[] {url}, "Ogiltig adress");
                } catch (ParsingFeedException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidrssurl",
                            new Object[] {url}, "Ogiltig adress");
                } catch (FeedException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidrssurl",
                            new Object[] {url}, "Ogiltig adress");
                } catch (IOException e) {
                    errors.rejectValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "invalidurl",
                            new Object[] {url}, "Ogiltig adress");
                }
            }
        }
    }
}