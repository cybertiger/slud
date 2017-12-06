package org.cyberiantiger.slud.net;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TelnetSocketChannelHandler extends SocketChannelHandler {
    private static final Logger log = LoggerFactory.getLogger(TelnetSocketChannelHandler.class);
    private static final int BUFFER_SIZE = 0x100000;
    @Getter
    private final TelnetCodec telnetCodec;

    public TelnetSocketChannelHandler() throws IOException {
        super(BUFFER_SIZE, BUFFER_SIZE);
        telnetCodec = new TelnetCodec(this);
    }

    @Override
    public void handleConnect() throws IOException {
        super.handleConnect();
        telnetCodec.handleConnect();
    }

    @Override
    public void handleRead() throws IOException {
        super.handleRead();
        telnetCodec.handleRead(getReadBuffer());
    }

    @Override
    public void handleWrite() throws IOException {
        super.handleWrite();
    }
}
