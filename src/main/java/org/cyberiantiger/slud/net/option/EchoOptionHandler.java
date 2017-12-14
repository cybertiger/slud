package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetCodec;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
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
    }

    @Override
    public void handleDont() {
    }

    @Override
    public void handleWill() {
        handler.get().addUiAction(ui -> ui.setLocalEcho(false));
    }

    @Override
    public void handleWont() {
        handler.get().addUiAction(ui -> ui.setLocalEcho(true));
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
        // ignored
    }
}
