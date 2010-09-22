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
package se.vgregion.portal.rss.client.service;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Anders Asplund - Callista Enterprise
 * 
 */
public final class StringTools {

    private StringTools() {}

    /**
     * General purpose string replacement utility.
     *
     * @param text - template to be worked upon.
     * @param searchString - placeholder.
     * @param replaceKeys - keys to resolve.
     * @param replaceValues - map of resolution possibilities.
     * @return a set of resolved strings, one for each key. 
     */
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
