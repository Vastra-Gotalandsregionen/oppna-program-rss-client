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
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;

/**
 * Validating the WrapperBean.
 * 
 * @author Simon Göransson - Monator Technologies AB
 * 
 */

@Component
public class PortletPreferencesWrapperBeanValidator implements Validator {

    private final FeedFetcher feedFetcher;

    /**
     * Validator must have a FeedFetcher to work.
     * 
     * @param feedFetcher
     *            the FeedFetcher
     */
    @Autowired
    public PortletPreferencesWrapperBeanValidator(FeedFetcher feedFetcher) {
        this.feedFetcher = feedFetcher;
    }

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
     * @param target
     *            The {@link PortletPreferencesWrapperBean} to validate
     * @param errors
     *            An error object provided by the spring container potentially containing the validation errors.
     */
    @Override
    public void validate(Object target, Errors errors) {
        PortletPreferencesWrapperBean bean = (PortletPreferencesWrapperBean) target;

        if (bean.getNumberOfItems1() == null || !bean.getNumberOfItems1().matches("^\\d+")) {
            errors.rejectValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1, "invalidnoofitems",
                    "Antalet inlägg måste vara större än noll");
        }
        if (bean.getNumberOfItems2() == null || !bean.getNumberOfItems2().matches("^\\d+")) {
            errors.rejectValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEM_2, "invalidnoofitems",
                    "Antalet inlägg måste vara större än noll");
        }
        if (bean.getNumberOfItems3() == null || !bean.getNumberOfItems3().matches("^\\d+")) {
            errors.rejectValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEM_3, "invalidnoofitems",
                    "Antalet inlägg måste vara större än noll");
        }
        if (bean.getNumberOfItems4() == null || !bean.getNumberOfItems4().matches("^\\d+")) {
            errors.rejectValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEM_4, "invalidnoofitems",
                    "Antalet inlägg måste vara större än noll");
        }

        validateUrl(errors, bean.getRssFeedLink1(), PortletPreferencesWrapperBean.RSS_FEED_LINK_1);
        validateUrl(errors, bean.getRssFeedLink2(), PortletPreferencesWrapperBean.RSS_FEED_LINK_2);
        validateUrl(errors, bean.getRssFeedLink3(), PortletPreferencesWrapperBean.RSS_FEED_LINK_3);
        validateUrl(errors, bean.getRssFeedLink4(), PortletPreferencesWrapperBean.RSS_FEED_LINK_4);

    }

    private void validateUrl(Errors errors, String urls, String field) {
        URL feedUrl;

        System.out.println("urls = " + urls);

        if (urls != null) {
            for (String url : urls.split("\n")) {
                try {
                    feedUrl = new URL(url);
                    // As validation, try to fetch feed
                    feedFetcher.retrieveFeed(feedUrl);

                } catch (MalformedURLException e) {
                    errors.rejectValue(field, "invalidurl", new Object[]{url}, "Ogiltig adress");
                } catch (IllegalArgumentException e) {
                    errors.rejectValue(field, "invalidurl", new Object[]{url}, "Ogiltig adress");
                } catch (ParsingFeedException e) {
                    errors.rejectValue(field, "invalidrssurl", new Object[]{url}, "Ogiltig adress");
                } catch (FeedException e) {
                    errors.rejectValue(field, "invalidrssurl", new Object[]{url}, "Ogiltig adress");
                } catch (FetcherException e) {
                    errors.rejectValue(field, "invalidrssurl", new Object[]{url}, "Ogiltig adress");
                } catch (ConnectException e) {
                    errors.rejectValue(field, "invalidrssurl", new Object[]{url}, "Ogiltig adress");
                } catch (IOException e) {
                    errors.rejectValue(field, "invalidurl", new Object[]{url}, "Ogiltig adress");
                }
            }
        }
    }
}