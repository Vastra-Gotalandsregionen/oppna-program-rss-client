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
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

public class FeedEntryBean implements Serializable, Comparable<FeedEntryBean> {
    public static final int SHORT_EXCERPT_LENGTH = 200;
    private static final long serialVersionUID = 2L;
    private String title;
    private String excerpt;
    private String shortExcerpt;
    private List<String> contents;
    private String contentsString = "";
    private String link;
    private String feedTitle;
    private Date publishedDate;

    public static final Comparator<FeedEntryBean> GROUP_BY_SOURCE = new Comparator<FeedEntryBean>() {
        @Override
        public int compare(FeedEntryBean feedEntryBean1, FeedEntryBean feedEntryBean2) {
            return new CompareToBuilder().append(feedEntryBean1.feedTitle, feedEntryBean2.feedTitle)
                    .toComparison();
        }

        @Override
        public String toString() {
            return "GROUP_BY_SOURCE";
        };
    };

    public static final Comparator<FeedEntryBean> SORT_BY_DATE = new Comparator<FeedEntryBean>() {
        @Override
        public int compare(FeedEntryBean feedEntryBean1, FeedEntryBean feedEntryBean2) {
            return new CompareToBuilder().append(feedEntryBean2.publishedDate, feedEntryBean1.publishedDate)
                    .toComparison();
        }

        @Override
        public String toString() {
            return "SORT_BY_DATE";
        };
    };

    @SuppressWarnings("unchecked")
    public FeedEntryBean(SyndEntry syndEntry, String feedTitle) {
        if (syndEntry.getTitle() != null) {
            title = escapeText(syndEntry.getTitle().trim());
        }
        if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null) {
            excerpt = escapeText(syndEntry.getDescription().getValue().trim());
        }
        if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null
                && syndEntry.getDescription().getValue().length() > SHORT_EXCERPT_LENGTH) {
            shortExcerpt = StringUtils.abbreviate(excerpt, SHORT_EXCERPT_LENGTH);
        } else {
            shortExcerpt = excerpt;
        }

        contents = syndEntry.getContents();

        if (syndEntry.getContents() != null && syndEntry.getContents().size() > 0) {
            for (int i = 0; i < syndEntry.getContents().size(); i++) {
                SyndContent syndContent = (SyndContent) syndEntry.getContents().get(i);
                if (!StringUtils.isBlank(syndContent.getValue())) {
                    contentsString += syndContent.getValue();
                }
            }
        } else {
            contentsString = excerpt;
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
        output = input.replaceAll("\\u000A", "<br/>");
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

    @Override
    public int compareTo(FeedEntryBean o) {
        return FeedEntryBean.SORT_BY_DATE.compare(this, o);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}