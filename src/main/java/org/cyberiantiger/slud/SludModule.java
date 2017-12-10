package org.cyberiantiger.slud;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import org.cyberiantiger.slud.net.Backend;
import org.cyberiantiger.slud.net.BackendImpl;
import org.cyberiantiger.slud.net.Network;
import org.cyberiantiger.slud.net.NetworkImpl;
import org.cyberiantiger.slud.ui.SludUi;
import org.cyberiantiger.slud.ui.Ui;
import org.cyberiantiger.slud.ui.UiImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.Selector;

@Module
public class SludModule {
    private static final Logger log = LoggerFactory.getLogger(SludModule.class);
    private Slud main;

    public SludModule(Slud main) {
        this.main = main;
    }

    @Provides
    @Singleton
    public Selector getSelector() {
        try {
            return Selector.open();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Provides
    @Singleton
    public Backend getBackend(BackendImpl backend) {
        log.info("Starting backend");
        backend.start();
        return backend;
    }

    @Provides
    @Singleton
    public Slud getMain() {
        return main;
    }

    @Provides
    @Singleton
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public Network getNetwork(NetworkImpl network) {
        return network;
    }

    @Provides
    @Singleton
    public Ui getUi(UiImpl uiImpl) {
        return uiImpl;
    }
}
