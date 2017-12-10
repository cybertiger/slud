package org.cyberiantiger.slud.net;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;


public class ConnectionImpl implements Connection {

    private final TelnetSocketChannelHandler telnetSocketChannelHandler;

    @Inject
    public ConnectionImpl(TelnetSocketChannelHandler telnetSocketChannelHandler) {
        this.telnetSocketChannelHandler = telnetSocketChannelHandler;
    }

    @Override
    public void sendGmcp(String type, Object data) {
        // TODO
    }

    @Override
    public void sendCommand(String command) {
        telnetSocketChannelHandler.getWriteBuffer().put(command.getBytes(StandardCharsets.UTF_8));
        telnetSocketChannelHandler.getWriteBuffer().put((byte) 0x0a); // New line.
    }

    @Override
    public void disconnect() {
        try {
            telnetSocketChannelHandler.getChannel().close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
