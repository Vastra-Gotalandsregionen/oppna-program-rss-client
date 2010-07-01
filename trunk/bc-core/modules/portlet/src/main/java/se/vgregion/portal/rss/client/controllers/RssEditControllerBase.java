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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBeanValidator;

/**
 * Controller base for edit mode.
 */
public class RssEditControllerBase {
    private Logger logger = LoggerFactory.getLogger(RssEditControllerBase.class);

    /**
     * JSP to show when fatal error occurred.
     */
    public static final String ERROR_JSP = "fatalError";

    /**
     * Property key name for users feed URLs setting.
     */
    public static final String PREFERENCES = "portletPreferencesWrapperBean";

    @Autowired
    protected PortletPreferencesWrapperBeanValidator validator;

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
                logger.error("Error when trying to store RSS Client preferences.", e);
                model.addAttribute("saveError", "true");
            }
        } else {
            model.addAttribute("errors", result);
        }
    }
}