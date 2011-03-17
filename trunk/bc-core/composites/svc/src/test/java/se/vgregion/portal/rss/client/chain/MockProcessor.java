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

package se.vgregion.portal.rss.client.chain;

import java.util.*;

public class MockProcessor extends StringTemplatePlaceholderProcessor {

    @Override
    protected Set<String> getKeys(long userId) {
        if (userId == 0L) {
            return new HashSet<String>(Arrays.asList("key1"));
        } else if (userId == 1L) {
            return new HashSet<String>(Arrays.asList("key1", "key2"));
        } else if (userId == 2L) {
            return new HashSet<String>(Arrays.asList("key1", "key2", "key3"));
        } else if (userId == 3L) {
                return new HashSet<String>(Arrays.asList("nokey"));
        } else {
            return new HashSet<String>();
        }
    }

    @Override
    protected Map<String, String> getReplaceValues() {
        Map<String, String> replaceValues = new HashMap<String, String>();
        replaceValues.put("key1", "value1");
        replaceValues.put("key2", "value2");
        replaceValues.put("key3", "value3");
        return replaceValues;
    }
}