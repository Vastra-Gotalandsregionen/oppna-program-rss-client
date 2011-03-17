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
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anders Asplund - Callista Enterprise
 * @author David Rosell - Redpill-Linpro
 */
public abstract class StringTemplatePlaceholderProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(StringTemplatePlaceholderProcessor.class);

    // The next element in the chain of responsibility
    private StringTemplatePlaceholderProcessor next;
    private String placeholder;

    /**
     * Next processor to be called in the chain. If not set processing will terminate.
     * 
     * @param spp
     *            - next template processor.
     */
    public void setNext(StringTemplatePlaceholderProcessor spp) {
        next = spp;
    }

    protected abstract Set<String> getKeys(long userId);

    protected abstract Map<String, String> getReplaceValues();

    /**
     * Main method to be called.
     * <p/>
     * Call this method on the first processor in the chain. The template will be resolved to a Set of strings
     * where every placeholder combination for the user context is realized.
     * 
     * @param text
     *            - template to be operated upon.
     * @param userId
     *            - user context key.
     * @return - Set of all ways the template can be resoled.
     */
    public Set<String> replacePlaceholders(String text, long userId) {
        Set<String> result = new HashSet<String>();
        Set<String> keys = getKeys(userId);
        LOGGER.debug("User keyset: {}", keys);

        Map<String, String> replaceValues = getReplaceValues();
        LOGGER.debug("Map from map-values: {}", replaceValues);
        result.addAll(replace(text, keys, replaceValues));

        if (next != null) {
            result = next.replacePlaceholders(result, userId);
        }

        return result;
    }

    private Set<String> replacePlaceholders(Set<String> texts, long userId) {
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
                LOGGER.debug("Key: {}, Replacement; {}", new Object[] { key, replacement });
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
     * @param placeholder
     *            - The placeholder string that will be resolved.
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
