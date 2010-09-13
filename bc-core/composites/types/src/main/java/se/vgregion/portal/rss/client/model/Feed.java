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

package se.vgregion.portal.rss.client.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import se.vgregion.portal.core.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "vgr_rss_feed")
public class Feed extends AbstractEntity<Feed, String> implements Serializable {

    private static final long serialVersionUID = 3657313333971337391L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _primaryKey;
    @Column(length = 50, unique = true)
    private String feedId;
    @Column(length = 1024)
    private String feedUrl;
    @ManyToMany
    @JoinTable(name = "vgr_feed_role", joinColumns = @JoinColumn(name = "feedPk"), inverseJoinColumns = @JoinColumn(name = "rolePk"))
    private List<Role> roleList;

    @SuppressWarnings("unused")
    private Feed() {
        // Used only by JPA!!
    }

    public Feed(String feedId) {
        this.feedId = feedId;
    }

    @Override
    public String getId() {
        return feedId;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

}
