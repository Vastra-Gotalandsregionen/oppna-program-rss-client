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

package se.vgregion.portal.rss.client.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.rss.blacklist.BlackList;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator;
import se.vgregion.portal.rss.client.service.RssFetcherService;

/**
 * Controller base for edit mode.
 *
 * @author Simon Göransson - Monator Technologies AB
 */
public abstract class RssEditControllerBase {
    private Logger logger = LoggerFactory.getLogger(RssEditControllerBase.class);

    private final RssFetcherService rssFetcherService;
    private BlackList<String> blackList;

    /**
     * Edit controller.
     *
     * @param rssFetcherService the ReeFetcher service
     */
    public RssEditControllerBase(RssFetcherService rssFetcherService) {
        this.rssFetcherService = rssFetcherService;
    }

    /**
     * JSP to show when fatal error occurred.
     */
    public static final String ERROR_JSP = "fatalError";

    /**
     * Property key name for users feed URLs setting.
     */
    public static final String PREFERENCES = "portletPreferencesWrapperBean";

    @Autowired
    private PortletPreferencesWrapperBeanValidator validator;

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Method handling Action request, saving portlet preferences.
     *
     * @param model           RSS Client EDIT portlet's ModelMap
     * @param preferences     RSS Client portlet's PortletPreferences
     * @param preferencesBean Wrapper bean for portlet preferences
     * @param result          Spring object representing the binding of web view element and java object
     */
    @ActionMapping("save")
    public final void savePreferences(final ModelMap model, final PortletPreferences preferences,
                                      @ModelAttribute PortletPreferencesWrapperBean preferencesBean,
                                      BindingResult result) {

    	System.out.println("RssEditControllerBase - save - before validate");
    	
        validator.validate(preferencesBean, result);
        
        System.out.println("RssEditControllerBase - save - after validate");
        
        

        if (!result.hasErrors()) {
            try {
                // Set preference and store value
            	
            	System.out.println("RssEditControllerBase - save - before store");
            	
                preferencesBean.store(preferences);
                
                System.out.println("RssEditControllerBase - save - after store");
                
                model.addAttribute("saveError", null);
                // response.setPortletMode(PortletMode.VIEW); This did not work well since the preferences page was
                // opened initially every time.
            } catch (Exception e) {
                logger.error("Error when trying to store RSS Client preferences.", e);
                
                System.out.println("preferencesBean.store exception");
                System.out.print(e);
                
                model.addAttribute("saveError", "true");
            }
        } else {
            model.addAttribute("errors", result);
        }
    }

    /**
     * Blacklist action method. Remove all url's from blacklist.
     *
     * @param preferences PortletPreferences
     */
    @ActionMapping("clearFeedBlackList")
    public void clearFeedBlackList(final PortletPreferences preferences) {
        blackList.clear();
    }

    /**
     * Blacklist action method. Remove one feedlink from blacklist.
     *
     * @param feedLink Feed link
     */
    @ActionMapping("removeFromFeedBlackList")
    public void removeFromFeedBlackList(@RequestParam("feedLink") String feedLink) {
        blackList.remove(feedLink);
    }

    /**
     * Method handling Render request, fetching portlet preferences for view.
     *
     * @param model           RSS Client EDIT portlet's ModelMap
     * @param preferences     RSS Client portlet's PortletPreferences
     * @param preferencesBean Wrapper bean for portlet preferences
     * @param result          Spring object representing the binding of web view element and java object
     * @return jsp url for view
     * @throws ReadOnlyException Thrown if portlet container does not succeeds changing the portlet preferences
     */
    @RenderMapping
    public final String showPreferences(final ModelMap model, PortletPreferences preferences,
                                        @ModelAttribute PortletPreferencesWrapperBean preferencesBean,
                                        BindingResult result)
            throws ReadOnlyException {
        // Check if save action rendered an error
        if ("true".equals(model.get("saveError"))) {
            logger.error("Save action rendered an error");
            return ERROR_JSP;
        }

        // Add black list
        preferencesBean.setFeedBlackList(getFilteredFeedBlackList(preferences));

        // Don't replace values if we have a binding error from save action
        if (model.get("errors") == null) {
            preferencesBean.setNumberOfItems1(preferences.getValue(
                    PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1,
                    String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)));
            preferencesBean.setNumberOfItems2(preferences.getValue(
                    PortletPreferencesWrapperBean.NUMBER_OF_ITEM_2,
                    String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)));
            preferencesBean.setNumberOfItems3(preferences.getValue(
                    PortletPreferencesWrapperBean.NUMBER_OF_ITEM_3,
                    String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)));
            preferencesBean.setNumberOfItems4(preferences.getValue(
                    PortletPreferencesWrapperBean.NUMBER_OF_ITEM_4,
                    String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)));
            preferencesBean.setRssFeedLink1(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_LINK_1, ""));
            preferencesBean.setRssFeedLink2(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_LINK_2, ""));
            preferencesBean.setRssFeedLink3(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_LINK_3, ""));
            preferencesBean.setRssFeedLink4(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_LINK_4, ""));
            preferencesBean.setRssStandardClientPortletLink(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_STD_CLIENT_LINK, ""));
            preferencesBean.setRssFeedTitle1(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_TITLE_1, ""));
            preferencesBean.setRssFeedTitle2(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_TITLE_2, ""));
            preferencesBean.setRssFeedTitle3(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_TITLE_3, ""));
            preferencesBean.setRssFeedTitle4(preferences.getValue(
                    PortletPreferencesWrapperBean.RSS_FEED_TITLE_4, ""));
        } else {
            // Copy binding error from save action
            result.addAllErrors((BindingResult) model.get("errors"));
        }

        model.addAttribute(PREFERENCES, preferencesBean);
        return getConfigJsp();
    }

    protected abstract String getConfigJsp();

    @Autowired
    public void setBlackList(BlackList<String> blackList) {
        this.blackList = blackList;
    }

    private List<String> getFilteredFeedBlackList(final PortletPreferences preferences) {
        List<String> returnList = new ArrayList<String>();

        String url1 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_1, "");
        if (!StringUtils.isBlank(url1)) {
            if (blackList.contains(url1)) {
                returnList.add(url1);
            }
        }

        String url2 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_2, "");
        if (!StringUtils.isBlank(url2)) {
            if (blackList.contains(url2)) {
                returnList.add(url2);
            }
        }

        String url3 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_3, "");
        if (!StringUtils.isBlank(url3)) {
            if (blackList.contains(url3)) {
                returnList.add(url3);
            }
        }

        String url4 = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINK_4, "");
        if (!StringUtils.isBlank(url4)) {
            if (blackList.contains(url4)) {
                returnList.add(url4);
            }
        }

        return returnList;
    }
}