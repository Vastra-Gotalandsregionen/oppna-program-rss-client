package se.vgregion.portal.rss.blacklist;

import java.util.Collection;

/**
 * Interface representing a generic blacklist.
 *
 * @author Anders Asplund
 *
 * @param <T>
 */
public interface BlackList<T> {

    /**
     * Add an item.
     *
     * @param item the item
     */
    void add(T item);

    /**
     * Remove an item.
     *
     * @param item the item
     */
    void remove(T item);

    /**
     * Whether the item is contained in the object instance.
     *
     * @param item the item
     * @return <code>true</code> if the object contains the item or <code>false</code> otherwise
     */
    boolean contains(T item);

    /**
     * Clear/reset the object instance.
     */
    void clear();

    /**
     * Get the items as a {@link Collection}.
     *
     * @return the items as a {@link Collection}
     */
    Collection<T> items();
}
