package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class NawsOptionHandler extends AbstractOptionHandler {
    private static final Logger log = LoggerFactory.getLogger(NawsOptionHandler.class);
    private Lazy<TelnetSocketChannelHandler> handler;
    private int width;
    private int height;
    private boolean doNaws = false;

    @Inject
    public NawsOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        super(handler, TOPT_NAWS, true, false, true, false);
    }

    @Override
    protected void onLocal(boolean allow) {
        if (allow) {
            sendNawsSb();
        }
    }

    @Override
    protected void onRemote(boolean allow) {
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
    }

    public void setTerminalSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (isLocal()) {
            sendNawsSb();
        }
    }

    private void sendNawsSb() {
        byte[] packet = new byte[4];
        packet[0] = (byte) ((height >> 8) & 0xff);
        packet[1] = (byte) (height & 0xff);
        packet[2] = (byte) ((width >> 8) & 0xff);
        packet[3] = (byte) (width & 0xff);
        sendSuboption(ByteBuffer.wrap(packet));
    }
}
