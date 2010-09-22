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
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Anders Asplund - Callista Enterprise
 * @author David Rosell - Redpill-Linpro
 */
public class UserOrganizationProcessor extends StringTemplatePlaceholderProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOrganizationProcessor.class);

    private long companyId;

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
                organizationNames.add(org.getName().toLowerCase().replace(' ', '_'));
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

    public void setReplaceValues(File mapFile) throws ConfigurationException, UnsupportedEncodingException {
        try {
            LOGGER.debug("Map: {}", mapFile.getAbsolutePath());
            PropertiesConfiguration pc = new PropertiesConfiguration(mapFile);

            replaceValues = new HashMap<String, String>();
            for (@SuppressWarnings("unchecked")
            Iterator<String> it = pc.getKeys(); it.hasNext();) {
                String key = it.next();
                String value = pc.getString(key);
                LOGGER.debug("Key: {} Value: {}", new Object[]{key, value});
                System.out.println("Key: " + key + " Value: " + value);
                if (!StringUtils.isBlank(value)) {
                    value = (urlValueEncoding) ? URLEncoder.encode(value, "utf-8") : value;
                    key = key.toLowerCase();
                    LOGGER.debug("Key: {} Value: {}", new Object[]{key, value});
                    replaceValues.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encoding failure in mapping file [" + mapFilePathErrorMessage(mapFile) + "]", e);
        } catch (ConfigurationException e) {
            LOGGER.error("Failed to load replaceValues mapping file [" + mapFilePathErrorMessage(mapFile) + "]", e);
        }
    }

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
