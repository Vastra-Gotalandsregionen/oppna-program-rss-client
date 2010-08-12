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

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import se.vgregion.portal.rss.client.controllers.RssEditControllerBase;
import se.vgregion.portal.rss.client.service.RssFetcherService;

/**
 * Controller for edit mode.
 */
@Controller
@RequestMapping("EDIT")
public class RssMinimalEditController extends RssEditControllerBase {

    /**
     * JSP to show on success.
     */
    public static final String CONFIG_JSP = "preferencesConfigMinimal";

    @Autowired
    public RssMinimalEditController(RssFetcherService rssFetcherService) {
        super(rssFetcherService);
        super.setLogger(LoggerFactory.getLogger(RssMinimalEditController.class));
    }

    @Override
    protected String getConfigJsp() {
        return CONFIG_JSP;
    }
}