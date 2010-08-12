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

package se.vgregion.portal.rss.client.controllers.minimal;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.controllers.RssViewControllerBase;

/**
 * Controller for view mode, display of RSS items.
 * 
 * @author Jonas Liljenfeldt
 * @author anders asplund
 */
@Controller
@RequestMapping("VIEW")
public class RssMinimalViewController extends RssViewControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssMinimalViewController.class);

    public RssMinimalViewController() {
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
     * @throws IOException
     *             If I/O problems occur
     */
    @RenderMapping
    public String viewRssItemList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) throws IOException {

        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());

        List<FeedEntryBean> sortedRssEntries = getSortedRssEntries(model, preferences);

        sortedRssEntries = getItemsToBeDisplayed(preferences, sortedRssEntries);

        if (bundle != null) {
            response.setTitle(bundle.getString("javax.portlet.title") + " (" + sortedRssEntries.size() + ")");
        }
        response.setContentType("text/html");
        model.addAttribute("rssEntries", sortedRssEntries);
        return "rssFeedViewMinimal";
    }
}