package org.cyberiantiger.slud.net;

public interface Connection {

    void sendGmcp(String type, Object data);

    void sendCommand(String command);

    void disconnect();

}
