package org.cyberiantiger.slud.net;

public interface Connection {

    /**
     * Send a GMCP message.
     * @param type GMCP message type.
     * @param data GMCP data.
     */
    void sendGmcp(String type, Object data);

    /**
     * Send a command.
     * @param command Command to send.
     */
    void sendCommand(String command);

    /**
     * Disconnect.
     */
    void disconnect();

    /**
     * Set terminal size.
     */
    void setTerminalSize(int w, int h);


}
