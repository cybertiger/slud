package org.cyberiantiger.slud.net;

import dagger.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class GmcpOptionHandler implements TelnetCodec.OptionHandler {
    private CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
    private static final Logger log = LoggerFactory.getLogger(GmcpOptionHandler.class);
    private Lazy<TelnetSocketChannelHandler> handler;

    @Inject
    public GmcpOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        this.handler = handler;
    }

    @Override
    public byte getOption() {
        return TOPT_GMCP;
    }

    @Override
    public void handleConnect() {
        handler.get().getWriteBuffer().put(new byte[] { IAC, DO, TOPT_GMCP });
    }

    @Override
    public void handleDo() {
        log.info("GMCP do");
        handler.get().getWriteBuffer().put(new byte[] { IAC, WILL, TOPT_GMCP });
    }

    @Override
    public void handleDont() {
        log.info("GMCP dont");
    }

    @Override
    public void handleWill() {
        log.info("GMCP will");
    }

    @Override
    public void handleWont() {
        log.info("GMCP wont");
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
        try {
            log.info("Got GMCP data: {}", decoder.decode(data));
        } catch (CharacterCodingException ex) {
            log.error("Error decoding GMCP data", ex);
        }
    }
}
