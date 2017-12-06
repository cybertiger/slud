package org.cyberiantiger.slud;

import dagger.Module;
import dagger.Provides;
import org.cyberiantiger.slud.net.Backend;
import org.cyberiantiger.slud.ui.SludUi;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;

@Module
public class SludModule {
    private Backend backend;
    private SludUi sludUi;

    public SludModule() {
        sludUi = new SludUi();
        try {
            backend = new Backend();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Provides
    @Singleton
    public Backend getBackend() {
        return backend;
    }

    @Provides
    @Singleton
    public SludUi getSludUi() {
        return sludUi;
    }
}
