/**
 * 
 */
package se.vgregion.portal.rss.client;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author Anders Asplund - Callista Enterprise
 * 
 */
public abstract class StringTemplatePlaceholderProcessor {
    // The next element in the chain of responsibility
    protected StringTemplatePlaceholderProcessor next;
    private String placeholder;

    public void setNext(StringTemplatePlaceholderProcessor spp) {
        next = spp;
        // return spp;
    }

    abstract protected Set<String> getKeys(String context);

    abstract protected Map<String, String> getReplaceValues();

    public Set<String> replacePlaceholders(String text, String userId) {
        Set<String> result = new HashSet<String>();
        Set<String> keys = getKeys(userId);

        result.addAll(replace(text, keys, getReplaceValues()));

        if (next != null) {
            result = next.replacePlaceholders(result, userId);
        }

        return result;
    }

    public Set<String> replacePlaceholders(Set<String> texts, String userId) {
        Set<String> result = new HashSet<String>();
        for (String text : texts) {
            result.addAll(replacePlaceholders(text, userId));
        }
        return result;
    }

    private Set<String> replace(String text, Set<String> replaceKeys, Map<String, String> replaceValues) {

        final Set<String> results = new HashSet<String>();
        for (String key : replaceKeys) {
            String replacement = replaceValues.get(key);
            String result = text.replace(placeholder, StringUtils.trimToEmpty(replacement));
            results.add(result);
        }
        return results;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
