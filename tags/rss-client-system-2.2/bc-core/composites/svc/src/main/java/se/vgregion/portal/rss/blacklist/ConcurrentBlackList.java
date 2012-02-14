package se.vgregion.portal.rss.blacklist;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anders Asplund
 *
 * @param <T>
 */
public class ConcurrentBlackList<T> implements BlackList<T> {

    private final Set<T> blackList = Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());

    public void add(T feed) {
        blackList.add(feed);
    }

    public void remove(T feed) {
        blackList.remove(feed);
    }

    public boolean contains(T feed) {
        return blackList.contains(feed);
    }

    public void clear() {
        blackList.clear();
    }

    public Collection<T> items() {
        return Collections.unmodifiableSet(blackList);
    }
}
