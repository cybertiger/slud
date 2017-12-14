package org.cyberiantiger.slud.net;

import org.cyberiantiger.slud.SludComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Class to find all the useful object in the network scope and in the darkness bind them.
 *
 * Also manages creation and destruction of connections.
 */
public class NetworkImpl implements Network {
    private static final Logger log = LoggerFactory.getLogger(NetworkImpl.class);
    private SludComponent mainComponent;
    private Connection currentConnection = null;
    private Backend backend;
    private int terminalWidth = 80;
    private int terminalHeight = 24;

    @Inject
    public NetworkImpl(SludComponent mainComponent, Backend backend) {
        this.mainComponent = mainComponent;
        this.backend = backend;
    }

    @Override
    public void connect(String host, int port, String terminal) {
        if (currentConnection != null) {
            log.info("Disconnecting current connection");
            currentConnection.disconnect();
        }
        log.info("Creating new connection");
        currentConnection =
                mainComponent.getConnectionComponent(
                        new ConnectionModule(host, port, terminal)).getConnection();
        currentConnection.setTerminalSize(terminalWidth, terminalHeight);
    }

    @Override
    public void disconnect() {
        if (currentConnection != null) {
            currentConnection.disconnect();
        }
    }

    @Override
    public void sendGmcp(String type, Object data) {
        if (currentConnection != null) {
            currentConnection.sendGmcp(type, data);
        }
    }

    @Override
    public void sendCommand(String command) {
        if (currentConnection != null) {
            currentConnection.sendCommand(command);
        }
    }

    @Override
    public void setTerminalSize(int terminalWidth, int terminalHeight) {
        this.terminalWidth = terminalWidth;
        this.terminalHeight = terminalHeight;
        if (currentConnection != null) {
            currentConnection.setTerminalSize(terminalWidth, terminalHeight);
        }
    }
}
