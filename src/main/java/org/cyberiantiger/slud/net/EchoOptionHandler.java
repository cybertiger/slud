package org.cyberiantiger.slud.net;

import dagger.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.TOPT_ECHO;

public class EchoOptionHandler implements TelnetCodec.OptionHandler {
    private static final Logger log = LoggerFactory.getLogger(EchoOptionHandler.class);
    private Lazy<TelnetSocketChannelHandler> handler;

    @Inject
    public EchoOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        this.handler = handler;
    }

    @Override
    public byte getOption() {
        return TOPT_ECHO;
    }

    @Override
    public void handleConnect() {
    }

    @Override
    public void handleDo() {
        log.info("DO");
    }

    @Override
    public void handleDont() {
        log.info("DONT");
    }

    @Override
    public void handleWill() {
        log.info("WILL");
        handler.get().addUiAction(ui -> ui.setLocalEcho(false));
    }

    @Override
    public void handleWont() {
        log.info("WONT");
        handler.get().addUiAction(ui -> ui.setLocalEcho(true));
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
        // ignored
    }
}
