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

package se.vgregion.portal.rss.client.controllers.intra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.rss.client.beans.FeedEntryBean;
import se.vgregion.portal.rss.client.beans.PortletPreferencesWrapperBean;
import se.vgregion.portal.rss.client.controllers.RssViewControllerBase;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for view mode, display of RSS items.
 * 
 * @author Jonas Liljenfeldt
 * @author anders asplund
 * @author David Rosell
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

        addUserToModel(model, request);

        ResourceBundle bundle = getPortletConfig().getResourceBundle(response.getLocale());

        List<FeedEntryBean> sortedRssEntries =
                getSortedRssEntries(preferences, model, PortletPreferencesWrapperBean.RSS_FEED_LINK_1);

        sortedRssEntries =
                getItemsToBeDisplayed(preferences, sortedRssEntries,
                        PortletPreferencesWrapperBean.NUMBER_OF_ITEM_1);

        if (bundle != null) {
            response.setTitle(bundle.getString("javax.portlet.title") + " (" + sortedRssEntries.size() + ")");
        }
        response.setContentType("text/html");
        model.addAttribute("rssEntries", sortedRssEntries);
        return "rssFeedViewMinimal";
    }
}