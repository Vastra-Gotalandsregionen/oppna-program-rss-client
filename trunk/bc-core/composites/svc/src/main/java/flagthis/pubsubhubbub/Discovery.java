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

/*
 *
 *@author royans K tharakan | http://royans.net/ | rkt@pobox.com
 * Released under Apache License 2.0
 *
 */

package flagthis.pubsubhubbub;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

public class Discovery {

	public Discovery() {
	}

	public String hasHub(String feed) throws Exception {
		return null;
	}

	public String getContents(String feed) throws Exception {
		String response = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(feed);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httpget, responseHandler);
		response = (responseBody);

		httpclient.getConnectionManager().shutdown();

		return response;
	}

	public String getHub(String feedurl) throws Exception {
		DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = Factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(
				getContents(feedurl))));

		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		XPathExpression xPathExpression;
		String hub;
		xPathExpression = xPath.compile("/feed/link[@rel='hub']/@href");
		hub = (String) xPathExpression.evaluate(doc);
		if ((hub==null)||(hub=="")){
			xPathExpression = xPath.compile("//link[@rel='hub']/@href");
			hub = (String) xPathExpression.evaluate(doc);			
		}
		return hub;
	}

	public HashMap<String, String> getHubs(ArrayList<String> feedurls) {
		Iterator<String> i = feedurls.iterator();
		HashMap<String, String> hashtable = new HashMap<String, String>();
		while (i.hasNext()) {
			String feedurl = i.next();
			try {
				hashtable.put(feedurl, getHub(feedurl));
			} catch (Exception e) {
			}
		}
		return hashtable;
	}
}
