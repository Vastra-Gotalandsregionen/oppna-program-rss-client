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
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator;
import se.vgregion.portal.rss.client.service.RssFetcherService;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller base for edit mode.
 */
public abstract class RssEditControllerBase {
    private Logger logger = LoggerFactory.getLogger(RssEditControllerBase.class);

    private RssFetcherService rssFetcherService;

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
     * @param model
     *            RSS Client EDIT portlet's ModelMap
     * @param preferences
     *            RSS Client portlet's PortletPreferences
     * @param preferencesBean
     *            Wrapper bean for portlet preferences
     * @param result
     *            Spring object representing the binding of web view element and java object
     */
    @ActionMapping("save")
    public final void savePreferences(final ModelMap model, final PortletPreferences preferences,
            @ModelAttribute PortletPreferencesWrapperBean preferencesBean, BindingResult result) {

        validator.validate(preferencesBean, result);

        if (!result.hasErrors()) {
            try {
                // Set preference and store value
                preferencesBean.store(preferences);
                model.addAttribute("saveError", null);
                // response.setPortletMode(PortletMode.VIEW); This did not work well since the preferences page was
                // opened initially every time.
            } catch (Exception e) {
                logger.error("Error when trying to store RSS Client preferences.", e);
                model.addAttribute("saveError", "true");
            }
        } else {
            model.addAttribute("errors", result);
        }
    }

    /**
     * Blacklist action method.
     * Remove all url's from blacklist.
     *
     * @param preferences PortletPreferences
     */
    @ActionMapping("clearFeedBlackList")
    public void clearFeedBlackList(final PortletPreferences preferences) {
        String feedLinks = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "");
        if (!StringUtils.isBlank(feedLinks)) {
            for (String url : feedLinks.split("\n")) {
                rssFetcherService.removeFromFeedBlackList(url);
            }
        }
    }

    /**
     * Blacklist action method.
     * Remove one feedlink from blacklist.
     *
     * @param feedLink Feed link
     */
    @ActionMapping("removeFromFeedBlackList")
    public void removeFromFeedBlackList(@RequestParam("feedLink") String feedLink) {
        rssFetcherService.removeFromFeedBlackList(feedLink);
    }

    /**
     * Method handling Render request, fetching portlet preferences for view.
     * 
     * @param model
     *            RSS Client EDIT portlet's ModelMap
     * @param preferences
     *            RSS Client portlet's PortletPreferences
     * @param preferencesBean
     *            Wrapper bean for portlet preferences
     * @param result
     *            Spring object representing the binding of web view element and java object
     * @throws ReadOnlyException
     *             Thrown if portlet container does not succeeds changing the portlet preferences
     * @return jsp url for view
     */
    @RenderMapping
    public final String showPreferences(final ModelMap model, PortletPreferences preferences,
            @ModelAttribute PortletPreferencesWrapperBean preferencesBean, BindingResult result)
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
            preferencesBean.setNumberOfItems(
                    preferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS,
                    String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)));
            preferencesBean.setNumberOfExcerptRows(
                    preferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_EXCERPT_ROWS,
                    String.valueOf(PortletPreferencesWrapperBean.DEFAULT_NUMBER_OF_EXCERPT_ROWS)));
            preferencesBean.setRssFeedLinks(
                    preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, ""));
            preferencesBean.setRssStandardClientPortletLink(
                    preferences.getValue(PortletPreferencesWrapperBean.RSS_STD_CLIENT_LINK, ""));
        } else {
            // Copy binding error from save action
            result.addAllErrors((BindingResult) model.get("errors"));
        }

        model.addAttribute(PREFERENCES, preferencesBean);
        return getConfigJsp();
    }

    protected abstract String getConfigJsp();

    private List<String> getFilteredFeedBlackList(final PortletPreferences preferences) {
        List<String> returnList = new ArrayList<String>();
        String feedLinks = preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, "");
        List<String> blackList = rssFetcherService.getFeedBlackList();

        if (!StringUtils.isBlank(feedLinks)) {
            for (String url : feedLinks.split("\n")) {
                if (blackList.contains(url)) {
                    returnList.add(url);
                }
            }
        }
        return returnList;
    }
}