package org.cyberiantiger.slud.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class TelnetSocketChannelHandler extends SocketChannelHandler {
    private static final Logger log = LoggerFactory.getLogger(TelnetSocketChannelHandler.class);
    private static final int BUFFER_SIZE = 0x100000;
    private final TelnetCodec telnetCodec;

    @Inject
    public TelnetSocketChannelHandler(SocketChannel channel, TelnetCodec telnetCodec) {
        super(channel, BUFFER_SIZE, BUFFER_SIZE);
        this.telnetCodec = telnetCodec;
    }


    @Override
    public void handleConnect() throws IOException {
        log.info("Connected");
        super.handleConnect();
        telnetCodec.handleConnect();
    }

    @Override
    public void handleRead() throws IOException {
        log.info("handleRead()");
        super.handleRead();
        telnetCodec.handleRead(getReadBuffer());
    }

    @Override
    public void handleWrite() throws IOException {
        log.info("handleWrite()");
        super.handleWrite();
    }

    @Override
    public void handleClose() throws IOException {
        // TODO
        log.info("handleClose()");
    }
}
