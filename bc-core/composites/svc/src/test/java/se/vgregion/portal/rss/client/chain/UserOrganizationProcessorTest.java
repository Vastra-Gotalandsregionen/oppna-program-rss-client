package se.vgregion.portal.rss.client.chain;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.UserLocalService;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserOrganizationProcessorTest {
    UserOrganizationProcessor proc;

    File propertiesFile;
    Writer w;

    @Mock
    UserLocalService userLocalServiceMock;

    @Mock
    OrganizationLocalService organizationLocalServiceMock;

    @Mock
    Organization orgMock;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        propertiesFile = File.createTempFile("tmp", ".properties");
        w = new OutputStreamWriter(new FileOutputStream(propertiesFile), "UTF-8");

        proc = new UserOrganizationProcessor(organizationLocalServiceMock);
    }


    @After
    public void tearDown() throws IOException {
        w.close();
    }

    @Test
    public void testSetReplaceValuesSimple() throws Exception {
        String key = "Regionservice";
        String value = "regionservice";

        writeToFile(key, value);

        proc.setReplaceValues(propertiesFile);
        Map<String, String> result = proc.getReplaceValues();

        assertEquals(1, result.size());
        assertEquals(key.toLowerCase(), result.keySet().iterator().next());
        assertEquals(value, result.get(key.toLowerCase()));

    }

    @Test
    public void testSetReplaceValuesWithSpaceInKey() throws Exception {
        String key = "Skaraborgs_sjukhus";
        String value = "skaraborgs sjukhus";

        writeToFile(key, value);

        proc.setReplaceValues(propertiesFile);
        Map<String, String> result = proc.getReplaceValues();

        assertEquals(1, result.size());
        assertEquals(key.toLowerCase(), result.keySet().iterator().next());
        assertEquals(URLEncoder.encode(value, "utf-8"), result.get(key.toLowerCase()));

    }

    @Test
    public void testSetReplaceValuesWithÄ() throws Exception {
        String key = "Kungälvs_sjukhus";
        String value = "kungälvs sjukhus";

        writeToFile(key, value);

        proc.setReplaceValues(propertiesFile);
        Map<String, String> result = proc.getReplaceValues();

        assertEquals(1, result.size());
        assertEquals(key.toLowerCase(), result.keySet().iterator().next());
        assertEquals(URLEncoder.encode(value, "utf-8"), result.get(key.toLowerCase()));

    }

    @Test
    public void testSetReplaceValuesWithSpecial() throws Exception {
        String key = "Folkhälsokommittén_åäö_ÅÄÖ_&_-";
        String value = "kungälvs sjukhus åäö ÅÄÖ & -";

        writeToFile(key, value);

        proc.setReplaceValues(propertiesFile);
        Map<String, String> result = proc.getReplaceValues();

        assertEquals(1, result.size());
        assertEquals(key.toLowerCase(), result.keySet().iterator().next());
        assertEquals(URLEncoder.encode(value, "utf-8"), result.get(key.toLowerCase()));

    }

    @Test
    public void testSetReplaceValuesWithBlankValue() throws Exception {
        String key = "Folkhälsokommittén_åäö_ÅÄÖ_&_-";
        String value = "";

        writeToFile(key, value);

        proc.setReplaceValues(propertiesFile);
        Map<String, String> result = proc.getReplaceValues();

        assertEquals(0, result.size());
    }

    @Test
    public void testGetKeys() throws SystemException, PortalException {
        String orgName = "Name ÅÄÖ åäö";
        List<Organization> orgList = Arrays.asList(new Organization[]{orgMock});

        given(userLocalServiceMock.getUserIdByScreenName(anyLong(), anyString())).willReturn(0l);
        given(organizationLocalServiceMock.getUserOrganizations(anyLong())).willReturn(orgList);
        given(orgMock.getName()).willReturn(orgName);

        Set<String> result = proc.getKeys(0L);

        assertEquals(result.size(), 1);
        assertEquals(orgName.toLowerCase().replace(' ', '_'), result.iterator().next());

    }

    private void writeToFile(String key, String value) throws IOException, ConfigurationException {
        w.append(key).append("=").append(value);
        w.flush();
    }
}
