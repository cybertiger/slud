package org.cyberiantiger.slud.net;

import org.cyberiantiger.slud.Slud;
import org.cyberiantiger.slud.ui.Ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TelnetSocketChannelHandler extends SocketChannelHandler {
    private static final Logger log = LoggerFactory.getLogger(TelnetSocketChannelHandler.class);
    private static final int BUFFER_SIZE = 0x100000;
    private final TelnetCodec telnetCodec;
    private final Slud main;
    private final List<Consumer<Ui>> actions = new ArrayList<>();

    @Inject
    public TelnetSocketChannelHandler(SocketChannel channel, TelnetCodec telnetCodec, Slud main) {
        super(channel, BUFFER_SIZE, BUFFER_SIZE);
        this.telnetCodec = telnetCodec;
        this.main = main;
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
        main.runInUi(actions);
        actions.clear();
    }

    @Override
    public void handleWrite() throws IOException {
        super.handleWrite();
    }

    @Override
    public void handleClose() throws IOException {
        // TODO
        log.info("handleClose()");
    }

    public void addUiAction(Consumer<Ui> action) {
        actions.add(action);
    }
}
