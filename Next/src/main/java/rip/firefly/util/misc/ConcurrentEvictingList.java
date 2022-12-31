package rip.firefly.util.misc;

import lombok.Getter;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @param <E> List Element
 */
@Getter
public final class ConcurrentEvictingList<E> extends ConcurrentLinkedDeque<E> {

    private final int maxSize;

    /**
     * This constructor will create a
     * new list object.
     *
     * @param maxSize Max List Size
     */
    public ConcurrentEvictingList(final int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * This constructor will copy an old collection
     * into the new list object.
     *
     * @param collection Previous collection to copy
     * @param maxSize Max List Size
     */
    public ConcurrentEvictingList(final Collection<? extends E> collection, final int maxSize) {
        super(collection);
        this.maxSize = maxSize;
    }

    /**
     * @param e The object to add to the list
     * @return If the object was added successfully
     */
    @Override
    public boolean add(final E e) {
        if (size() >= getMaxSize()) removeFirst();
        return super.add(e);
    }

    /**
     * @return If the list is full
     */
    public boolean isFull() {
        return size() >= getMaxSize();
    }
}