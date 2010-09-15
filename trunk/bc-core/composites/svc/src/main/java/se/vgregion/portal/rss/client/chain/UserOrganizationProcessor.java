/**
 * 
 */
package se.vgregion.portal.rss.client.chain;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.UserLocalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        Map<String, String> replaceValues = new HashMap<String, String>();
        replaceValues.put("Alingsås Lasarett", "Alings%C3%A5s+Lasarett");
        replaceValues.put("Folkhögskolor", "h%C3%A4lso-+och+sjukv%C3%A5rdskansliet+göteborg");
        replaceValues.put("Habilitering & Hälsa", "%22h%C3%A4lso-+och+sjukv%C3%A5rdskansliet+mariestad%22");
        return replaceValues;
    }

}
