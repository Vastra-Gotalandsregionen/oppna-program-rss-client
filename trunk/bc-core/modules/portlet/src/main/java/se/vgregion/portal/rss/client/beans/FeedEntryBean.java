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

package se.vgregion.portal.rss.client.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.sun.syndication.feed.synd.SyndEntry;

public class FeedEntryBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String excerpt;
    private List<String> contents;
    private String link;
    private String feedTitle;
    private Date publishedDate;

    @SuppressWarnings("unchecked")
    public FeedEntryBean(SyndEntry syndEntry, String feedTitle) {
        if (syndEntry.getTitle() != null) {
            title = escapeText(syndEntry.getTitle().trim());
        }
        if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null) {
            excerpt = escapeText(syndEntry.getDescription().getValue().trim());
            for (char c : excerpt.toCharArray()) {
                System.out.print(">" + c + '<');
                System.out.println(">" + new Integer(c).intValue() + '<');
            }
        }
        contents = syndEntry.getContents();
        if (syndEntry.getLink() != null) {
            link = syndEntry.getLink().trim();
        }
        publishedDate = syndEntry.getPublishedDate();
        this.feedTitle = escapeText(feedTitle);
    }

    private String escapeText(String input) {
        String output = "";
        output = input.replace("'", "\\'");
        output = input.replaceAll("\\u000A", "\\\\n");
        return output;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

}