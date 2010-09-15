/**
 * 
 */
package se.vgregion.portal.rss.client.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author Anders Asplund - Callista Enterprise
 * 
 */
public final class StringTools {

    public static Set<String> replace(String text, String searchString, Set<String> replaceKeys,
            Map<String, String> replaceValues) {

        final Set<String> results = new HashSet<String>();
        for (String key : replaceKeys) {
            String replacement = replaceValues.get(key);
            String result = text.replace(searchString, StringUtils.trimToEmpty(replacement));
            results.add(result);
        }
        return results;
    }
}
