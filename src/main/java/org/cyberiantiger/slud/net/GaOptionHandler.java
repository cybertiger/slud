package org.cyberiantiger.slud.net;

import lombok.AllArgsConstructor;

import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.*;

@AllArgsConstructor
public class GaOptionHandler implements TelnetCodec.OptionHandler {
    TelnetCodec codec;

    @Override
    public void handleConnect() {
        codec.getChannelHandler().getWriteBuffer().put(new byte[] { IAC, DONT, TOPT_SUPP});
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
