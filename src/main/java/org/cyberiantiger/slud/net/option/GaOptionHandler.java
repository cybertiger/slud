package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import lombok.AllArgsConstructor;
import org.cyberiantiger.slud.net.TelnetCodec;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class GaOptionHandler implements TelnetCodec.OptionHandler {

    private final Lazy<TelnetSocketChannelHandler> handler;

    @Inject
    public GaOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        this.handler = handler;
    }

    @Override
    public byte getOption() {
        return TOPT_SUPP;
    }

    @Override
    public void handleConnect() {
        handler.get().getWriteBuffer().put(new byte[] { IAC, DONT, TOPT_SUPP});
    }

    @Override
    public void handleDo() {
    }

    @Override
    public void handleDont() {
    }

    @Override
    public void handleWill() {
    }

    @Override
    public void handleWont() {
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
    }
}
