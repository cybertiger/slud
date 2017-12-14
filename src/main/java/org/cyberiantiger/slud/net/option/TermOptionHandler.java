package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetCodec;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class TermOptionHandler implements TelnetCodec.OptionHandler {
    private static final Logger log = LoggerFactory.getLogger(TermOptionHandler.class);
    private Lazy<TelnetSocketChannelHandler> handler;
    private boolean doTerm = false;
    private byte[] term;

    @Inject
    public TermOptionHandler(Lazy<TelnetSocketChannelHandler> handler, @Named("term") String term) {
        this.handler = handler;
        // TODO: Escape IAC in term type string.
        this.term = term.getBytes(StandardCharsets.UTF_8);
    }

        @Override
    public byte getOption() {
        return TOPT_TERM;
    }

    @Override
    public void handleConnect() {
        handler.get().getWriteBuffer().put(new byte[] { IAC, WILL, TOPT_TERM});
    }

    @Override
    public void handleDo() {
        doTerm = true;
        log.info("DO TERM");
        sendTermSb();
    }

    @Override
    public void handleDont() {
        log.info("DONT TERM");
        doTerm = false;
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

    private void sendTermSb() {
        byte[] packet = new byte[term.length + 6];
        int i = 0;
        packet[i++] = IAC;
        packet[i++] = SB;
        packet[i++] = TOPT_TERM;
        packet[i++] = IS;
        System.arraycopy(term,0 , packet, i, term.length);
        i+= term.length;
        packet[i++] = IAC;
        packet[i] = SE;
        log.info("Sending TERM SB");
        handler.get().getWriteBuffer().put(packet);
    }
}
