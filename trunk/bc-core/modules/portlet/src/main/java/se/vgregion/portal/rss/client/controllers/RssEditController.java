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

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Controller for edit mode.
 */
@Controller
@RequestMapping("EDIT")
public class RssEditController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RssEditController.class);
    
    /**
     * JSP to show on success.
     */
    public static final String CONFIG_JSP = "preferencesConfig";

    /**
     * JSP to show when fatal error occurred.
     */
    public static final String ERROR_JSP = "fatalError";

    /**
     * Property key name for users feed URLs setting.
     */
    public static final String CONFIG_RSS_FEED_LINK_KEY = "rssFeedLink";

    /**
     * Method handling Render request, fetching portlet preferences for view.
     * 
     * @param model
     *            RSS Client EDIT portlet's ModelMap
     * @param preferences
     *            RSS Client portlet's PortletPreferences
     * @return jsp url for view
     */
    @RenderMapping
    public final String showPreferences(final ModelMap model, final PortletPreferences preferences) {
        // Check if save action rendered an error
        if ("true".equals(model.get("saveError"))) {
            return ERROR_JSP;
        }

        // Set preferences key
        model.addAttribute(CONFIG_RSS_FEED_LINK_KEY, preferences.getValue(CONFIG_RSS_FEED_LINK_KEY, ""));
        return CONFIG_JSP;
    }

    /**
     * Method handling Action request, saving portlet preferences.
     * 
     * @param model
     *            RSS Client EDIT portlet's ModelMap
     * @param preferences
     *            RSS Client portlet's PortletPreferences
     * @param feedLinks
     *            User feed URLs
     */
    @ActionMapping
    public final void savePreferences(final ModelMap model, final PortletPreferences preferences, ActionResponse response,
            @RequestParam(value = CONFIG_RSS_FEED_LINK_KEY, required = false) final String feedLinks) {
        try {
            // Set preference and store value
            preferences.setValue(CONFIG_RSS_FEED_LINK_KEY, feedLinks);
            preferences.store();
            model.addAttribute("saveError", null);
//            response.setPortletMode(PortletMode.VIEW); This did not work well since the preferences page was opened initially every time.
        } catch (Exception e) {
            LOGGER.error("Error when trying to store RSS Client preferences.", e);
            model.addAttribute("saveError", "true");
        }
    }
}
