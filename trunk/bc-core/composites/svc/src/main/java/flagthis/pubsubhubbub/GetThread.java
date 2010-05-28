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

package flagthis.pubsubhubbub;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

final class GetThread extends Thread {

	private final HttpClient httpClient;
	private final HttpContext context;
	private final HttpPost httppost;
	public HttpResponse httpresponse = null;

	public GetThread(HttpClient httpClient, HttpPost httppost) {
		this.httpClient = httpClient;
		this.context = new BasicHttpContext();
		this.httppost = httppost;
	}

	@Override
	public void run() {
		try {
			httpresponse = this.httpClient.execute(this.httppost, this.context);
			HttpEntity entity = httpresponse.getEntity();
			if (entity != null) {
				System.out.println(httpresponse.getStatusLine());
				entity.consumeContent();
			}
		} catch (Exception ex) {
			this.httppost.abort();
		}
	}


}