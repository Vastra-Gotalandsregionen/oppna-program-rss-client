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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedEntryBean implements Serializable, Comparable<FeedEntryBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedEntryBean.class);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * The limit for the short version of the news item description.
     */
    public static final int SHORT_EXCERPT_LENGTH = 200;
    private static final long serialVersionUID = 2L;
    private String title;
    private String excerpt;
    private List<String> contents;
    private String contentsString = "";
    private String link;
    private String feedTitle;
    private Date publishedDate;
    private String enclosureType;
    private String enclosureUrl;

    // startdate, starttime, enddate, endtime och location

    private Date startDate;
    private Date startTime;
    private Date endTime;
    private Date endDate;
    private String location;

    /**
     * Group-by-source comparator.
     */
    public static final Comparator<FeedEntryBean> GROUP_BY_SOURCE = new Comparator<FeedEntryBean>() {
        @Override
        public int compare(FeedEntryBean feedEntryBean1, FeedEntryBean feedEntryBean2) {
            return new CompareToBuilder()
                    .append(feedEntryBean1.getFeedTitle(), feedEntryBean2.getFeedTitle()).toComparison();
        }

        @Override
        public String toString() {
            return "GROUP_BY_SOURCE";
        }
    };

    /**
     * Sort-by-date comparator.
     */
    public static final Comparator<FeedEntryBean> SORT_BY_DATE = new Comparator<FeedEntryBean>() {
        @Override
        public int compare(FeedEntryBean feedEntryBean1, FeedEntryBean feedEntryBean2) {
            return new CompareToBuilder().append(feedEntryBean2.getPublishedDate(),
                    feedEntryBean1.getPublishedDate()).toComparison();
        }

        @Override
        public String toString() {
            return "SORT_BY_DATE";
        }
    };

    /**
     * Create instance from SyndEntry object and feed title.
     *
     * @param syndEntry Rome representation of an news item
     * @param feedTitle The name of the feed
     * @param excerptLen The maximum length which the description is cut to
     */
    @SuppressWarnings("unchecked")
    public FeedEntryBean(SyndEntry syndEntry, String feedTitle, int excerptLen) {
        Object fm = syndEntry.getForeignMarkup();
        if (syndEntry.getTitle() != null) {
            title = escapeText(syndEntry.getTitle().trim());
        }
        if (syndEntry.getDescription() != null && syndEntry.getDescription().getValue() != null) {
            excerpt = calculateExcerpt(syndEntry.getDescription().getValue().trim(), excerptLen);
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
            if (syndEntry.getDescription() != null) {
                contentsString = removeTags(syndEntry.getDescription().getValue().trim());
            } else {
                contentsString = "";
            }
        }

        if (syndEntry.getLink() != null) {
            link = syndEntry.getLink().trim();
        }

        if (syndEntry.getEnclosures() != null) {
            List<SyndEnclosure> syndEnclosures = syndEntry.getEnclosures();
            if (!syndEnclosures.isEmpty()) {
                SyndEnclosure syndEnclosure = syndEnclosures.get(0);

                enclosureType = syndEnclosure.getType();
                enclosureUrl = syndEnclosure.getUrl();
            }
        }

        publishedDate = syndEntry.getPublishedDate();

        // Fallback on "updatedDate" which is used with Atom feeds.
        if (publishedDate == null) {
            publishedDate = syndEntry.getUpdatedDate();
        }

        this.feedTitle = escapeText(feedTitle);
    }

    private String removeTags(String input) {
        return input.replaceAll("\\<[^>]*>", "");
    }

    private String calculateExcerpt(String input, int excerptLen) {
        String text = removeTags(input);

        String suffix = "";
        int cut = excerptLen;
        if (text.length() > cut) {
            suffix = "...";
            text = text.substring(0, cut);
            cut = text.lastIndexOf(" ");
            text = text.substring(0, cut);
        }

        return text + suffix;
    }

    private String escapeText(String input) {
        String output = "";
        output = input.replace("'", "\\'");
        output = output.replaceAll("\\u000A", "<br/>");
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
        return (publishedDate == null) ? null : (Date) publishedDate.clone();
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = (publishedDate == null) ? null : (Date) publishedDate.clone();
    }

    public String getContentsString() {
        return contentsString;
    }

    public void setContentsString(String contentsString) {
        this.contentsString = contentsString;
    }

    public String getEnclosureType() {
        return enclosureType;
    }

    public void setEnclosureType(String enclosureType) {
        this.enclosureType = enclosureType;
    }

    public String getEnclosureUrl() {
        return enclosureUrl;
    }

    public void setEnclosureUrl(String enclosureUrl) {
        this.enclosureUrl = enclosureUrl;
    }

    @Override
    public int compareTo(FeedEntryBean o) {
        return FeedEntryBean.SORT_BY_DATE.compare(this, o);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedEntryBean)) {
            return false;
        }

        FeedEntryBean that = (FeedEntryBean) o;

        return new EqualsBuilder().append(title, that.title).append(link, that.link).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(link).hashCode();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = toDate(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = toTimeDate(startTime);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = toDate(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = toTimeDate(endTime);
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private Date toDate(String text) {
        try {
            if (text == null) {
                return null;
            }

            return (dateFormat.parse(text));
        } catch (NullPointerException e) {
            LOGGER.warn("Null date text.");
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private Date toTimeDate(String text) {
        try {
            return (timeFormat.parse(text));
        } catch (NullPointerException e) {
            LOGGER.warn("Null date text.");
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

}