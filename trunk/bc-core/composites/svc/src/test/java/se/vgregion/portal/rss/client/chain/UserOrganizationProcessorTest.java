package se.vgregion.portal.rss.client.chain;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserOrganizationProcessorTest {
    UserOrganizationProcessor proc;

    File propertiesFile;
    FileWriter fw;

    @Before
    public void setup() throws IOException {
        propertiesFile = File.createTempFile("tmp", ".properties");
        fw = new FileWriter(propertiesFile);

        proc = new UserOrganizationProcessor(); 
    }

    @After
    public void tearDown() throws IOException {
        fw.close();
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

    private void writeToFile(String key, String value) throws IOException, ConfigurationException {
        fw.append(key).append("=").append(value);
        fw.flush();
    }
}
