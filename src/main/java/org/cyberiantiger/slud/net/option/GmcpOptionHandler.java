package org.cyberiantiger.slud.net.option;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class GmcpOptionHandler extends AbstractOptionHandler {
    private CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
    private static final Logger log = LoggerFactory.getLogger(GmcpOptionHandler.class);
    private final ObjectMapper mapper;

    @Inject
    public GmcpOptionHandler(Lazy<TelnetSocketChannelHandler> handler, ObjectMapper mapper) {
        super(handler, TOPT_GMCP, true, true, true, true);
        this.mapper = mapper;
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
        try {
            log.info("Got GMCP data: {}", decoder.decode(data));
        } catch (CharacterCodingException ex) {
            log.error("Error decoding GMCP data", ex);
        }
    }

    @Override
    protected void onLocal(boolean allow) {
        log.info("We will send GMCP: {}", allow);
    }

    @Override
    protected void onRemote(boolean allow) {
        log.info("We will receive GMCP: {}", allow);
    }

    public void sendGmcp(String type, Object data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(type.getBytes(StandardCharsets.UTF_8));
            out.write(0x20); // Space.
            mapper.writeValue(out, data);
            sendSuboption(ByteBuffer.wrap(out.toByteArray()));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
