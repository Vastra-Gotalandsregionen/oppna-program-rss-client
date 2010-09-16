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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import se.vgregion.portal.rss.client.service.PropertiesUtil;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.UserLocalService;

/**
 * @author Anders Asplund - Callista Enterprise
 * 
 */
public class UserOrganizationProcessor extends StringTemplatePlaceholderProcessor {

    private final long companyId = 10112;

    @Autowired
    private UserLocalService userLocalService;

    @Autowired
    private OrganizationLocalService organizationLocalService;

    @Resource(name = "propertyConfigurer")
    private PropertiesUtil propertiesUtil;

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
        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return organizationNames;
    }

    @Override
    protected Map<String, String> getReplaceValues() {
        return propertiesUtil.getPropertiesMap();
    }

}
