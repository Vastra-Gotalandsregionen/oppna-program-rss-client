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

package se.vgregion.portal.rss.client.intra.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateUtil;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.controllers.RssViewControllerBase;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for view mode, display of RSS items.
 *
 * @author Jonas Liljenfeldt
 * @author Anders Asplund
 * @author David Rosell
 * @author Claes Lundahl
 * @author Erik Andersson
 */
@Controller
@RequestMapping("VIEW")
public class RssIntraViewController extends RssViewControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssIntraViewController.class);

    /**
     * Controller for minimal view portlet.
     */
    public RssIntraViewController() {
        super.setLogger(LOGGER);
    }

    /**
     * Shows RSS items for user.
     *
     * @param model
     *            ModelMap
     * @param request
     *            RenderRequest
     * @param response
     *            RenderResponse
     * @param preferences
     *            RSS client VIEW portlet's PortletPreferences
     * @return View name.
     * @throws
     * @throws java.io.IOException
     *             If I/O problems occur
     */
    @RenderMapping
    public String viewRssItemList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) throws IOException {
    	
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);
		
		long scopeGroupId = themeDisplay.getScopeGroupId();
		
    	PortletPreferences portletPreferences = request.getPreferences();
    	
    	// Note, Erik Andersson 2015-04-10. Realized that addUserToModel is required by RssViewControllerBase
    	// Code should be refactored.
    	addUserToModel(model, request);

        String feedTitle = preferences.getValue(
        		PortletPreferencesWrapperBean.RSS_FEED_TITLE_1, "");

        List<FeedEntryBean> sortedRssEntries = getSortedRssEntries(
        		preferences, model, PortletPreferencesWrapperBean.RSS_FEED_LINK_1);

        sortedRssEntries = getItemsToBeDisplayed(
        		preferences, sortedRssEntries, PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1);
        
        // Display Template
		String displayStyle = GetterUtil.getString(portletPreferences.getValue("displayStyle", ""));
		long displayStyleGroupId = GetterUtil.getLong(portletPreferences.getValue("displayStyleGroupId", null), scopeGroupId);
		
		Map<String, Object> displayTemplateContextObjects = new HashMap<String, Object>();
		displayTemplateContextObjects.put("feedTitle", feedTitle);
		
		long portletDisplayDDMTemplateId = PortletDisplayTemplateUtil.getPortletDisplayTemplateDDMTemplateId(
				displayStyleGroupId, displayStyle);
		
        model.addAttribute("feedTitle", feedTitle);
        model.addAttribute("portletDisplayDDMTemplateId", portletDisplayDDMTemplateId);
        model.addAttribute("displayTemplateContextObjects", displayTemplateContextObjects);
        model.addAttribute("rssEntries", sortedRssEntries);

        return "view";
    }

}
