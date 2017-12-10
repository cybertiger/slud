package org.cyberiantiger.slud;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import org.cyberiantiger.slud.net.*;

import javax.inject.Named;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Module
public class ConnectionModule {
    private String host;
    private int port;
    private String terminal;
    private int initTermWidth;
    private int initTermHeight;

    public ConnectionModule(String host, int port, String terminal, int width, int height) {
        this.host = host;
        this.port = port;
        this.terminal = terminal;
        this.initTermWidth = width;
        this.initTermHeight = height;
    }

    @Provides
    @Named("term")
    @ConnectionScope
    public String getTerminal() {
        return terminal;
    }

    @Provides
    @Named("initTermWidth")
    @ConnectionScope
    public int getInitTermWidth() {
        return initTermWidth;
    }

    @Provides
    @ConnectionScope
    @Named("initTermHeight")
    public int getInitTermHeight() {
        return initTermHeight;
    }

    @Provides
    @ConnectionScope
    public Connection getConnection(ConnectionImpl impl) {
        return impl;
    }

    @Provides
    @ConnectionScope
    public SocketChannel createChannel() {
        try {
            SocketChannel result = SocketChannel.open();
            result.configureBlocking(false);
            return result;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }


    @Provides
    @ConnectionScope
    public TelnetSocketChannelHandler makeConnection(Backend backend, SocketChannel connection, TelnetCodec telnetCodec) {
        try {
            TelnetSocketChannelHandler handler = new TelnetSocketChannelHandler(connection, telnetCodec);
            backend.register(handler);
            handler.getChannel().connect(new InetSocketAddress(host, port));
            return handler;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public TelnetCodec.OptionHandler getGaOptionHandler(GaOptionHandler gaOptionHandler) {
        return gaOptionHandler;
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public TelnetCodec.OptionHandler getGmcpOptionHandler(GmcpOptionHandler gmcpOptionHandler) {
        return gmcpOptionHandler;
    }
}
