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

package se.vgregion.portal.rss.client.view;

import java.util.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import se.vgregion.portal.rss.client.domain.RssItem;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

public class RssFeedView extends AbstractRssFeedView {
    private String feedTitle;
    private String feedDesc;
    private String feedLink;

    @Required
    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    @Required
    public void setFeedDescription(String feedDesc) {
        this.feedDesc = feedDesc;
    }

    @Required
    public void setFeedLink(String feedLink) {
        this.feedLink = feedLink;
    }

    @Override
    protected void buildFeedMetadata(Map model, Channel feed, HttpServletRequest request) {
        feed.setTitle(feedTitle);
        feed.setDescription(feedDesc);
        feed.setLink(feedLink);
    }

    @Override
    protected List<Item> buildFeedItems(Map model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        @SuppressWarnings("unchecked")
        List<RssItem> rssItems = (List<RssItem>) model.get("newsItemList");

        List<Item> feedItems = new ArrayList<Item>();
        for (RssItem newsItem : rssItems) {
            Item feedItem = new Item();
            feedItem.setTitle(newsItem.getTitle());
            feedItem.setAuthor(newsItem.getAuthor());
            feedItem.setPubDate(newsItem.getDatePublished());

            Description desc = new Description();
            desc.setType("text/html");
            desc.setValue(newsItem.getDescription());
            feedItem.setDescription(desc);

            feedItem.setLink(newsItem.getLink());
            feedItems.add(feedItem);
        }
        return feedItems;
    }
}
