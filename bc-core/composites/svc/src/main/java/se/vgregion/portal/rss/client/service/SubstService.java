package se.vgregion.portal.rss.client.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SubstService {
    Set<String> substitute(String target, List<String> keys, Map<String, String> values);
}
