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
    protected Set<String> getKeys(long userId) {
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
