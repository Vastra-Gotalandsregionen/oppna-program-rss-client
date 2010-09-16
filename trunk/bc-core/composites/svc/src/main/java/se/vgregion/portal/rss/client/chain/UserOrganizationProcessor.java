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

/**
 *
 */
package se.vgregion.portal.rss.client.chain;

import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.UserLocalService;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Anders Asplund - Callista Enterprise
 * @author David Rosell - Redpill-Linpro
 */
public class UserOrganizationProcessor extends StringTemplatePlaceholderProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOrganizationProcessor.class);

    private long companyId = 10112;

    @Autowired
    private UserLocalService userLocalService;

    @Autowired
    private OrganizationLocalService organizationLocalService;

    private Map<String, String> replaceValues;

    private boolean urlValueEncoding = true;

    @Override
    protected Set<String> getKeys(String userId) {
        Set<String> organizationNames = new HashSet<String>();
        try {
            long uid = userLocalService.getUserIdByScreenName(companyId, userId);
            List<Organization> organizations = organizationLocalService.getUserOrganizations(uid);
            for (Organization org : organizations) {
                organizationNames.add(org.getName());
                System.out.println(org.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return organizationNames;
    }

    @Override
    protected Map<String, String> getReplaceValues() {
        return replaceValues;
    }

    public void setReplaceValues(File mapFile) {
        try {
            LOGGER.debug("Map: " + mapFile.getAbsolutePath());
            PropertiesConfiguration pc = new PropertiesConfiguration(mapFile);

            replaceValues = new HashMap<String, String>();
            for (Iterator it = pc.getKeys(); it.hasNext();) {
                String key = (String) it.next();
                String value = pc.getString(key);
                value = (urlValueEncoding) ? URLEncoder.encode(value, "utf-8") : value;
                LOGGER.debug("Key: {} Value: {}", new Object[]{key, value});

                replaceValues.put(key, value);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load replaceValues mapping file [" + mapFilePathErrorMessage(mapFile) + "]", e);
        }
    }

    /*
        http://hitta.vgregion.se/opensearch/search?format=rss&q=%22h%C3%A4lso-+och+sjukv%C3%A5rdskansliet+mariestad%22&1facet_infotype=Nyhet&
        http://hitta.vgregion.se/opensearch/search?format=rss&q=%22h%C3%A4lso-+och+sjukv%C3%A5rdskansliet+g%C3%B6teborg%22&1facet_infotype=Nyhet&
     */

    private String mapFilePathErrorMessage(File mapFilePath) {
        if (mapFilePath == null) {
            return "No mapFile has been configured";
        } else {
            return mapFilePath.getAbsolutePath();
        }
    }

    public void setUrlValueEncoding(Boolean urlValueEncoding) {
        this.urlValueEncoding = urlValueEncoding;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
