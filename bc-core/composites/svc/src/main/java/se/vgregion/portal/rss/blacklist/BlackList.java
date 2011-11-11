package se.vgregion.portal.rss.blacklist;

import java.util.Collection;

/**
 * @author Anders Asplund
 *
 * @param <T>
 */
public interface BlackList<T> {

    void add(T feed);

    void remove(T feed);

    boolean contains(T feed);

    void clear();

    Collection<T> items();
}
