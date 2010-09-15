/**
 *
 */
package se.vgregion.portal.rss.client.chain;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Anders Asplund - Callista Enterprise
 * @author David Rosell - Redpill-Linpro
 */
public abstract class StringTemplatePlaceholderProcessor {
    // The next element in the chain of responsibility
    private StringTemplatePlaceholderProcessor next;
    private String placeholder;

    /**
     * Next processor to be called in the chain. If not set processing will terminate.
     *
     * @param spp - next template processor.
     */
    public void setNext(StringTemplatePlaceholderProcessor spp) {
        next = spp;
    }

    abstract protected Set<String> getKeys(String userId);

    abstract protected Map<String, String> getReplaceValues();

    /**
     * Main method to be called.
     * <p/>
     * Call this method on the first processor in the chain. The template will be resolved to a Set
     * of strings where every placeholder combination for the user context is realized.
     *
     * @param text   - template to be operated upon.
     * @param userId - user context key.
     * @return - Set of all ways the template can be resoled.
     */
    public Set<String> replacePlaceholders(String text, String userId) {
        Set<String> result = new HashSet<String>();
        Set<String> keys = getKeys(userId);

        result.addAll(replace(text, keys, getReplaceValues()));

        if (next != null) {
            result = next.replacePlaceholders(result, userId);
        }

        return result;
    }

    private Set<String> replacePlaceholders(Set<String> texts, String userId) {
        Set<String> result = new HashSet<String>();
        for (String text : texts) {
            result.addAll(replacePlaceholders(text, userId));
        }
        return result;
    }

    private Set<String> replace(String text, Set<String> replaceKeys, Map<String, String> replaceValues) {

        final Set<String> results = new HashSet<String>();
        if (text.contains(placeholder)) {
            for (String key : replaceKeys) {
                String replacement = replaceValues.get(key);
                if (replacement != null) {
                    String result = text.replace(placeholder, StringUtils.trimToEmpty(replacement));
                    results.add(result);
                }
            }
        } else {
            results.add(text);
        }
        return results;
    }

    /**
     * The placeholder has to be set to use a processor.
     *
     * @param placeholder - The placeholder string that will be resolved.
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
