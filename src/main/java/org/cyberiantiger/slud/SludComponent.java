package org.cyberiantiger.slud;

import dagger.Component;
import org.cyberiantiger.slud.net.Backend;
import org.cyberiantiger.slud.net.Network;
import org.cyberiantiger.slud.ui.SludUi;
import org.cyberiantiger.slud.ui.Ui;

import javax.inject.Singleton;

/**
 * Top level component holding the application state.
 */
@Component(modules = {SludModule.class})
@Singleton
public interface SludComponent {

    Slud getSlud();

    Network getNetwork();

    Ui getUi();

    Backend getBackend();

    ConnectionComponent getConnectionComponent(ConnectionModule module);
}
