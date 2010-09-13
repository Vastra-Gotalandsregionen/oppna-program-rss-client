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

package se.vgregion.portal.rss.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.rss.client.model.Feed;
import se.vgregion.portal.rss.client.model.FeedRepository;
import se.vgregion.portal.rss.client.model.Role;

import java.util.List;

@Service
public class FeedUrlServiceImpl implements FeedUrlService {

    @Autowired
    FeedRepository feedRepository;

    @Override
    public List<Feed> getFeedsByRole(Role role) {
        return feedRepository.findByRole(role);
    }

}
