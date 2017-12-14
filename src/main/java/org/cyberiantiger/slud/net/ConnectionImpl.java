package org.cyberiantiger.slud.net;

import org.cyberiantiger.slud.net.option.NawsOptionHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetOption.TOPT_NAWS;


public class ConnectionImpl implements Connection {

    private final TelnetSocketChannelHandler telnetSocketChannelHandler;
    private final NawsOptionHandler naws;

    @Inject
    public ConnectionImpl(TelnetSocketChannelHandler telnetSocketChannelHandler, TelnetCodec telnetCodec) {
        this.telnetSocketChannelHandler = telnetSocketChannelHandler;
        this.naws = telnetCodec.getOptionHandler(TOPT_NAWS);
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

    @Override
    public void setTerminalSize(int w, int h) {
        naws.setTerminalSize(w, h);
    }
}
