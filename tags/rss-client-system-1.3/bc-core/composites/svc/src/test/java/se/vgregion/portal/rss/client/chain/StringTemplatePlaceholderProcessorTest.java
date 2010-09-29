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

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class StringTemplatePlaceholderProcessorTest {
    StringTemplatePlaceholderProcessor startProc;
    StringTemplatePlaceholderProcessor otherProc;
    StringTemplatePlaceholderProcessor noProc;

    List<String> rssFeedTemplates;

    @Before
    public void setUp() throws Exception {
        startProc = new MockProcessor();
        startProc.setPlaceholder("{mock.placeholder}");
        
        otherProc = new MockProcessor();
        otherProc.setPlaceholder("{other.mock.placeholder}");

        noProc = new MockProcessor();
        noProc.setPlaceholder("{no.placeholder}");
    }

    @Test
    public void testReplaceNoPlaceholders() throws Exception {
        String text = "http://static.org?rss.xml";

        Set<String> result = noProc.replacePlaceholders(text, "OneKey");
        assertEquals(1, result.size());
        assertTrue(result.contains(text));

        result = noProc.replacePlaceholders(text, "TwoKey");
        assertEquals(1, result.size());
        assertTrue(result.contains(text));

        result = noProc.replacePlaceholders(text, "ThreeKey");
        assertEquals(1, result.size());
        assertTrue(result.contains(text));

        result = noProc.replacePlaceholders(text, "NoKey");
        assertEquals(1, result.size());
        assertTrue(result.contains(text));

        result = noProc.replacePlaceholders(text, "Nobody");
        assertEquals(1, result.size());
        assertTrue(result.contains(text));
    }

    @Test
    public void testReplaceOnePlaceholdersOneProcessor() throws Exception {
        String text = "http://static.org?{mock.placeholder}&rss.xml";

        Set<String> result = startProc.replacePlaceholders(text, "OneKey");
        assertEquals(1, result.size());
        assertTrue(result.contains("http://static.org?value1&rss.xml"));

        result = startProc.replacePlaceholders(text, "TwoKey");
        assertEquals(2, result.size());
        assertTrue(result.contains("http://static.org?value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&rss.xml"));

        result = startProc.replacePlaceholders(text, "ThreeKey");
        assertEquals(3, result.size());
        assertTrue(result.contains("http://static.org?value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&rss.xml"));

        result = startProc.replacePlaceholders(text, "NoKey");
        assertEquals(0, result.size());

        result = startProc.replacePlaceholders(text, "Nobody");
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceOnePlaceholdersChainTwoProcessor() throws Exception {
        startProc.setNext(noProc);

        String text = "http://static.org?{mock.placeholder}&rss.xml";

        Set<String> result = startProc.replacePlaceholders(text, "OneKey");
        assertEquals(1, result.size());
        assertTrue(result.contains("http://static.org?value1&rss.xml"));

        result = startProc.replacePlaceholders(text, "TwoKey");
        assertEquals(2, result.size());
        assertTrue(result.contains("http://static.org?value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&rss.xml"));

        result = startProc.replacePlaceholders(text, "ThreeKey");
        assertEquals(3, result.size());
        assertTrue(result.contains("http://static.org?value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&rss.xml"));

        result = startProc.replacePlaceholders(text, "NoKey");
        assertEquals(0, result.size());

        result = startProc.replacePlaceholders(text, "Nobody");
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceTwoPlaceholdersChainOneProcessor() throws Exception {
        String text = "http://static.org?{mock.placeholder}&{other.mock.placeholder}&rss.xml";

        Set<String> result = startProc.replacePlaceholders(text, "OneKey");
        assertEquals(1, result.size());
        assertTrue(result.contains("http://static.org?value1&{other.mock.placeholder}&rss.xml"));

        result = startProc.replacePlaceholders(text, "TwoKey");
        assertEquals(2, result.size());
        assertTrue(result.contains("http://static.org?value1&{other.mock.placeholder}&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&{other.mock.placeholder}&rss.xml"));

        result = startProc.replacePlaceholders(text, "ThreeKey");
        assertEquals(3, result.size());
        assertTrue(result.contains("http://static.org?value1&{other.mock.placeholder}&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&{other.mock.placeholder}&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&{other.mock.placeholder}&rss.xml"));

        result = startProc.replacePlaceholders(text, "NoKey");
        assertEquals(0, result.size());

        result = startProc.replacePlaceholders(text, "Nobody");
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceTwoPlaceholdersChainTwoProcessor() throws Exception {
        startProc.setNext(otherProc);

        String text = "http://static.org?{mock.placeholder}&{other.mock.placeholder}&rss.xml";

        Set<String> result = startProc.replacePlaceholders(text, "OneKey");
        assertEquals(1, result.size());
        assertTrue(result.contains("http://static.org?value1&value1&rss.xml"));

        result = startProc.replacePlaceholders(text, "TwoKey");
        assertEquals(4, result.size());
        assertTrue(result.contains("http://static.org?value1&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value1&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value2&rss.xml"));

        result = startProc.replacePlaceholders(text, "ThreeKey");
        assertEquals(9, result.size());
        assertTrue(result.contains("http://static.org?value1&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value1&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value1&value3&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value3&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&value3&rss.xml"));

        result = startProc.replacePlaceholders(text, "NoKey");
        assertEquals(0, result.size());

        result = startProc.replacePlaceholders(text, "Nobody");
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceTwoPlaceholdersThreeProcessor() throws Exception {
        startProc.setNext(otherProc);
        otherProc.setNext(noProc);

        String text = "http://static.org?{mock.placeholder}&{other.mock.placeholder}&rss.xml";

        Set<String> result = startProc.replacePlaceholders(text, "OneKey");
        assertEquals(1, result.size());
        assertTrue(result.contains("http://static.org?value1&value1&rss.xml"));

        result = startProc.replacePlaceholders(text, "TwoKey");
        assertEquals(4, result.size());
        assertTrue(result.contains("http://static.org?value1&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value1&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value2&rss.xml"));

        result = startProc.replacePlaceholders(text, "ThreeKey");
        assertEquals(9, result.size());
        assertTrue(result.contains("http://static.org?value1&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&value1&rss.xml"));
        assertTrue(result.contains("http://static.org?value1&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&value2&rss.xml"));
        assertTrue(result.contains("http://static.org?value1&value3&rss.xml"));
        assertTrue(result.contains("http://static.org?value2&value3&rss.xml"));
        assertTrue(result.contains("http://static.org?value3&value3&rss.xml"));

        result = startProc.replacePlaceholders(text, "NoKey");
        assertEquals(0, result.size());

        result = startProc.replacePlaceholders(text, "Nobody");
        assertEquals(0, result.size());
    }
}
