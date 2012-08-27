package se.vgregion.portal.rss.blacklist;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe implementation of {@link BlackList}.
 *
 * @author Anders Asplund
 *
 * @param <T>
 */
public class ConcurrentBlackList<T> implements BlackList<T> {

    private final Set<T> blackList = Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());

    /**
     * {@inheritDoc}
     */
    public void add(T feed) {
        blackList.add(feed);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T feed) {
        blackList.remove(feed);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(T feed) {
        return blackList.contains(feed);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        blackList.clear();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<T> items() {
        return Collections.unmodifiableSet(blackList);
    }
}
