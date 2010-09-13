package se.vgregion.portal.rss.client.service;

import se.vgregion.portal.rss.client.model.Feed;
import se.vgregion.portal.rss.client.model.Role;

import java.util.List;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface FeedUrlService {
    List<Feed> getFeedsByRole(Role role);
}
