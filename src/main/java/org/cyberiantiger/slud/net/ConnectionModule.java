package org.cyberiantiger.slud.net;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import org.cyberiantiger.slud.Slud;
import org.cyberiantiger.slud.net.option.*;

import javax.inject.Named;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;

import static org.cyberiantiger.slud.ui.Ui.ConnectionStatus.CONNECTING;

@Module
public class ConnectionModule {
    private String host;
    private int port;
    private String terminal;

    public ConnectionModule(String host, int port, String terminal) {
        this.host = host;
        this.port = port;
        this.terminal = terminal;
    }

    @Provides
    @Named("term")
    @ConnectionScope
    public String getTerminal() {
        return terminal;
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
            result.setOption(StandardSocketOptions.TCP_NODELAY, true);
            return result;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }


    @Provides
    @ConnectionScope
    public TelnetSocketChannelHandler makeConnection(
            Backend backend,
            SocketChannel connection,
            TelnetCodec telnetCodec,
            Slud main) {
        try {
            TelnetSocketChannelHandler handler = new TelnetSocketChannelHandler(connection, telnetCodec, main);
            backend.register(handler);
            handler.getChannel().connect(new InetSocketAddress(host, port));
            main.runInUi(ui -> ui.setConnectionStatus(CONNECTING));
            return handler;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public OptionHandler getGaOptionHandler(SuppOptionHandler suppOptionHandler) {
        return suppOptionHandler;
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public OptionHandler getGmcpOptionHandler(GmcpOptionHandler gmcpOptionHandler) {
        return gmcpOptionHandler;
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public OptionHandler getEchoOptionHandler(EchoOptionHandler echoOptionHandler) {
        return echoOptionHandler;
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public OptionHandler getNawsOptionHandler(NawsOptionHandler nawsOptionHandler) {
        return nawsOptionHandler;
    }

    @Provides
    @IntoSet
    @ConnectionScope
    public OptionHandler getTermOptionHandler(TermOptionHandler termOptionHandler) {
        return termOptionHandler;
    }
}
