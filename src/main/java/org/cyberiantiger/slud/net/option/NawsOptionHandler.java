package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetCodec;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class NawsOptionHandler implements TelnetCodec.OptionHandler {
    private static final Logger log = LoggerFactory.getLogger(NawsOptionHandler.class);
    private Lazy<TelnetSocketChannelHandler> handler;
    private int width;
    private int height;
    private boolean doNaws = false;

    @Inject
    public NawsOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        this.handler = handler;
    }

        @Override
    public byte getOption() {
        return TOPT_NAWS;
    }

    @Override
    public void handleConnect() {
        handler.get().getWriteBuffer().put(new byte[] { IAC, WILL, TOPT_NAWS});
    }

    @Override
    public void handleDo() {
        doNaws = true;
        sendNawsSb();
    }

    @Override
    public void handleDont() {
        doNaws = false;
    }

    @Override
    public void handleWill() {
    }

    @Override
    public void handleWont() {

    }

    @Override
    public void handleSuboption(ByteBuffer data) {
    }

    public void setTerminalSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (doNaws) {
            sendNawsSb();
        }
    }

    private void sendNawsSb() {
        byte[] packet = new byte[] {
                IAC, SB, TOPT_NAWS,
                (byte) ((height >> 8) & 0xff),
                (byte) (height & 0xff),
                (byte) ((width >> 8) & 0xff),
                (byte) (width & 0xff),
                IAC, SE};
        log.info("Sending NAWS SB ");
        handler.get().getWriteBuffer().put(packet);
    }
}
