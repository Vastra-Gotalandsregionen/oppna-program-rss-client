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

package se.vgregion.portal.rss.client.infrastructure;

import org.springframework.stereotype.Repository;

import se.vgregion.portal.core.infrastructure.persistence.jpa.JpaRepository;
import se.vgregion.portal.rss.client.model.Feed;
import se.vgregion.portal.rss.client.model.FeedRepository;
import se.vgregion.portal.rss.client.model.Role;

import javax.persistence.Query;
import java.util.List;

/**
 * Defining standard CRUD operations.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Repository
public class JpaFeedRepository extends JpaRepository<Feed, String> implements FeedRepository {

    @Override
    public List<Feed> findByRole(Role role) {
        String queryString = "select f from Feed f join f.roleList r where r.name = :roleName";
        Query query = entityManager.createQuery(queryString).setParameter("roleName", role.getName());
        return query.getResultList();
    }
}
