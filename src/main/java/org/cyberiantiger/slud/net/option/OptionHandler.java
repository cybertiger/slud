package org.cyberiantiger.slud.net.option;

import java.nio.ByteBuffer;

public interface OptionHandler {
    byte getOption();
    void handleConnect();
    void handleDo();
    void handleDont();
    void handleWill();
    void handleWont();
    void handleSuboption(ByteBuffer data);
    void sendSuboption(ByteBuffer data);
}
