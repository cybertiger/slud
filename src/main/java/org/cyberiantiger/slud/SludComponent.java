package org.cyberiantiger.slud;

import dagger.Component;
import org.cyberiantiger.slud.net.Backend;
import org.cyberiantiger.slud.ui.SludUi;

import javax.inject.Singleton;

@Component(modules = {SludModule.class})
@Singleton
public interface SludComponent {

    Backend getBackend();

    SludUi getUi();

}
