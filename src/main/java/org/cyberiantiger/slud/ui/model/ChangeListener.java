package org.cyberiantiger.slud.ui.model;

import java.util.EventListener;

public interface ChangeListener<T> extends EventListener {

    void stateChanged(T state);

}
