package org.cyberiantiger.slud.net;

public interface Network extends Connection {

    /**
     * Open a new connection.
     */
    void connect(String host, int port, String terminal);

    /**
     * Send command.
     */
    void sendCommand(String command);

}
