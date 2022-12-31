package rip.firefly.util.misc;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @param <E> List Element
 */
public final class EvictingList<E> extends LinkedList<E> {
    private int maxSize;

    public EvictingList(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * This constructor will copy an old collection
     * into the new list object.
     *
     * @param collection Previous collection to copy
     * @param maxSize Max List Size
     */
    public EvictingList(Collection<? extends E> collection, int maxSize) {
        super(collection);
        this.maxSize = maxSize;
    }


    /**
     * @return Max List size
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @param element Element whose presence in this collection is to be ensured
     * @return Is successfully added
     */
    @Override
    public boolean add(E element) {
        if (size() >= getMaxSize()) removeFirst();
        return super.add(element);
    }
}