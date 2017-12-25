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

import static org.cyberiantiger.slud.ui.Ui.ConnectionStatus.CONNECTED;
import static org.cyberiantiger.slud.ui.Ui.ConnectionStatus.DISCONNECTED;

public class TelnetSocketChannelHandler extends SocketChannelHandler {
    private static final Logger log = LoggerFactory.getLogger(TelnetSocketChannelHandler.class);
    private static final int BUFFER_SIZE = 0x100000;
    private final TelnetCodec telnetCodec;
    private final Slud main;
    private final List<Consumer<? super Ui>> actions = new ArrayList<>();

    @Inject
    public TelnetSocketChannelHandler(SocketChannel channel, TelnetCodec telnetCodec, Slud main) {
        super(channel, BUFFER_SIZE, BUFFER_SIZE);
        this.telnetCodec = telnetCodec;
        this.main = main;
    }


    @Override
    public boolean handleConnect() throws IOException {
        boolean keepOpen = super.handleConnect();
        if (keepOpen) {
            // Only handle connect if connect is successful.
            telnetCodec.handleConnect();
            main.runInUi(ui -> ui.setConnectionStatus(CONNECTED));
        }
        return keepOpen;
    }

    @Override
    public boolean handleRead() throws IOException {
        boolean result = super.handleRead();
        telnetCodec.handleRead(getReadBuffer());
        main.runInUi(actions);
        actions.clear();
        return result;
    }

    @Override
    public boolean handleWrite() throws IOException {
        return super.handleWrite();
    }

    @Override
    public void handleClose() throws IOException {
        super.handleClose();
        main.runInUi(ui -> ui.setConnectionStatus(DISCONNECTED));
    }

    public void addUiAction(Consumer<? super Ui> action) {
        actions.add(action);
    }
}
