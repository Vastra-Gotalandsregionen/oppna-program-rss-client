/**
 * 
 */
package se.vgregion.portal.rss.client.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Anders Asplund - Callista Enterprise
 * 
 */
public final class StringTools {

    public static Set<String> replace(String text, String searchString, Set<String> replaceKeys,
            Map<String, String> replaceValues) {
        Set<String> results = new HashSet<String>();

        for (String key : replaceKeys) {
            String result = text.replace(searchString, replaceValues.get(key));
            results.add(result);
        }

        return results;
    }

}
