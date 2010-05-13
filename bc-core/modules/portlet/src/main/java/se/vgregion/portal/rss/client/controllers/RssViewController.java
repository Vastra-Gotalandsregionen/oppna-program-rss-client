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
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.servlet.view.AbstractView;

import se.vgregion.portal.rss.client.domain.RssItem;
import se.vgregion.portal.rss.client.service.RssFetcherService;

/**
 * Displays rss items.
 * 
 * @author jonas liljenfeldt
 */
@Controller
@RequestMapping("VIEW")
public class RssViewController {

    /**
     * Default error message for DataAccessException.
     */
    public static final String ERROR_WHEN_ACCESSING_DATA_SOURCE = "Error when accessing data source";
    
    /**
     * View rss items page name.
     */
    public static final String VIEW_TASKS = "viewRssItems";

    private static final Logger LOGGER = LoggerFactory.getLogger(RssViewController.class);

    private AbstractView rssFeedView;
    
    public void setRssFeedView(AbstractView rssFeedView) {
        this.rssFeedView = rssFeedView;
    }

    private RssFetcherService rssFetcherService = null;

    @Autowired
    private PortletConfig portletConfig = null;

    public void setRssFetcherService(RssFetcherService rssFetcherService) {
        this.rssFetcherService = rssFetcherService;
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
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
     *            PortletPreferences
     * @return View name.
     */
    @RenderMapping()
    public String viewTaskList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) {
        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());

        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);

        List<RssItem> rssItemList = null;
        if (!"".equals(userId)) {
            try {
                rssItemList = rssFetcherService.getRssItemList(userId);
                // Set number of RSS items in Rss viewer portlet header.
                if (bundle != null) {
                    response.setTitle(bundle.getString("javax.portlet.title") + " (" + rssItemList.size() + ")");
                }
            } catch (DataAccessException dataAccessException) {
                ObjectError objectError;
                if (bundle != null) {
                    objectError = new ObjectError("DataAccessError", bundle.getString("error.DataAccessError"));
                } else {
                    objectError = new ObjectError("DataAccessError", ERROR_WHEN_ACCESSING_DATA_SOURCE);
                }
                LOGGER.error("Error when trying to fetch RSS items for user " + userId + ".", dataAccessException);
                model.addAttribute("errors", objectError);
            }
        } else {
            rssItemList = new ArrayList<RssItem>();
        }

        model.addAttribute("rssItemList", rssItemList);
        return VIEW_TASKS;
    }

    private String getUserId(Map<String, ?> attributes) {
        String userId = "";
        if (attributes != null) {
            userId = (String) attributes.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }
        return userId;
    }
}