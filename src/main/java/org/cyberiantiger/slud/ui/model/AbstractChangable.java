package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractChangable<U, V extends AbstractChangable<U, V>> implements Changeable<U, V> {
    private final Set<ChangeListener<V>> changeListeners = new HashSet<>();
    private final Lazy<U> parent;
    private boolean changed = false;

    protected AbstractChangable(Lazy<U> parent) {
        this.parent = parent;
    }

    @Override
    public final void addChangeListener(ChangeListener<V> listener) {
        changeListeners.add(listener);
    }

    @Override
    public final void removeChangeListener(ChangeListener<V> listener) {
        changeListeners.remove(listener);
    }

    @Override
    public void reset() {
        setChanged();
        for (Changeable<V, ?> child : getChildren()) {
            child.reset();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void flushChanges() {
        if (changed) {
            changed = false;
            for (ChangeListener<V> listener : changeListeners) {
                listener.stateChanged((V) this);
            }
        }
        for (Changeable<V, ?> child : getChildren()) {
            child.flushChanges();
        }
    }

    @Override
    public final void setChanged() {
        changed = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> Changeable<T, U> getParent() {
        return (Changeable<T, U>) parent.get();
    }
}
