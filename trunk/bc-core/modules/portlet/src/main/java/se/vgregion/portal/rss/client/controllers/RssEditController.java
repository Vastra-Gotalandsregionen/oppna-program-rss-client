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

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator;

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
    public static final String PREFERENCES = "portletPreferencesWrapperBean";

    @Autowired
    private PortletPreferencesWrapperBeanValidator validator;

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
            return ERROR_JSP;
        }

        validator.validate(preferencesBean, result);

        preferencesBean.setNumberOfItems(preferences.getValue(PortletPreferencesWrapperBean.NUMBER_OF_ITEMS,
                String.valueOf(PortletPreferencesWrapperBean.DEFAULT_MAX_NUMBER_OF_ITEMS)));
        preferencesBean.setNumberOfExcerptRows(preferences.getValue(
                PortletPreferencesWrapperBean.NUMBER_OF_EXCERPT_ROWS, String
                        .valueOf(PortletPreferencesWrapperBean.DEFAULT_NUMBER_OF_EXCERPT_ROWS)));
        preferencesBean.setRssFeedLinks(preferences.getValue(PortletPreferencesWrapperBean.RSS_FEED_LINKS, ""));
        model.addAttribute(PREFERENCES, preferencesBean);
        return CONFIG_JSP;
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
    @ActionMapping
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
                LOGGER.error("Error when trying to store RSS Client preferences.", e);
                model.addAttribute("saveError", "true");
            }
        } else {
            model.addAttribute("errors", result);
        }
    }
}