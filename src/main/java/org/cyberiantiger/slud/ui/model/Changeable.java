package org.cyberiantiger.slud.ui.model;

import java.util.Collection;
import java.util.List;

public interface Changeable<U, V> {

    /**
     * Add a change listener.
     * @param listener the change listener to add.
     */
    void addChangeListener(ChangeListener<V> listener);

    /**
     * Remove a change listener.
     * @param listener the change listener to remove.
     */
    void removeChangeListener(ChangeListener<V> listener);

    /**
     * If this object has change, send events to the listeners.
     */
    void flushChanges();

    /**
     * Flag this object as having changed, and any parent objects.
     */
    void setChanged();

    /**
     * Reset this changable object to default state, and children.
     */
    void reset();

    /**
     * Get the parent object.
     *
     * @return null if no parent, or the parent otherwise.
     */
    <T> Changeable<T, U> getParent();

    /**
     * Get children of this object.
     *
     * @return A list of child Changeable.
     */
    Collection<? extends Changeable<V, ?>> getChildren();

}
