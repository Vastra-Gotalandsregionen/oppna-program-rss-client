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
import java.rmi.server.UID;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;

public class FeedEntryBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String excerpt;
    private String shortExcerpt;
    private List<String> contents;
    private String contentsString = "";
    private String link;
    private String feedTitle;
    private Date publishedDate;

    @SuppressWarnings("unchecked")
    public FeedEntryBean(SyndEntry syndEntry, String feedTitle) {
        String uid = new UID().toString();
        id = uid.toString().replaceAll(":", "-");
        if (syndEntry.getTitle() != null) {
            title = escapeText(syndEntry.getTitle().trim());
        }
        if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null) {
            excerpt = escapeText(syndEntry.getDescription().getValue().trim());
        }
        if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null
                && syndEntry.getDescription().getValue().length() > 200) {
            shortExcerpt = excerpt.substring(0, 200);
        } else {
            shortExcerpt = excerpt;
        }

        contents = syndEntry.getContents();

        if (syndEntry.getContents() != null && syndEntry.getContents().size() > 0) {
            for (int i = 0; i < syndEntry.getContents().size(); i++) {
                SyndContentImpl syndContentImpl = (SyndContentImpl) syndEntry.getContents().get(i);
                if (!StringUtils.isBlank(syndContentImpl.getValue())) {
                    contentsString += syndContentImpl.getValue();
                }
            }
        }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortExcerpt() {
        return shortExcerpt;
    }

    public void setShortExcerpt(String shortExcerpt) {
        this.shortExcerpt = shortExcerpt;
    }

    public String getContentsString() {
        return contentsString;
    }

    public void setContentsString(String contentsString) {
        this.contentsString = contentsString;
    }

}