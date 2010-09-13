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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import se.vgregion.portal.core.domain.patterns.valueobject.AbstractValueObject;

@Entity
@Table(name = "vgr_rss_role")
public class Role extends AbstractValueObject<Role> implements Serializable {
    private static final long serialVersionUID = -4820873134308911786L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _primaryKey;

    @Column(name = "ROLE_NAME", length = 50, unique = true)
    private String name;

    // @ManyToOne(targetEntity = se.vgregion.portal.rss.client.model.Feed.class)
    // @JoinColumn(name = "FEED_ID", nullable = false)
    // private Feed feed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
