/**
 * 
 */
package se.vgregion.portal.rss.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Anders Asplund - Callista Enterprise
 * 
 */
public class InfotypeQueryProcessor extends StringTemplatePlaceholderProcessor {

    @Override
    protected Set<String> getKeys(String userId) {
        return new HashSet<String>(Arrays.asList("role1", "role2"));
    }

    @Override
    protected Map<String, String> getReplaceValues() {
        Map<String, String> replaceValues = new HashMap<String, String>();
        replaceValues.put("role1", "Blogg");
        replaceValues.put("role2", "Nyhet");
        return replaceValues;
    }

}
