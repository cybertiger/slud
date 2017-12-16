package org.cyberiantiger.slud.net.option;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Lazy;
import org.cyberiantiger.slud.model.GmcpTypeHandler;
import org.cyberiantiger.slud.model.GmcpTypeHandlers;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.cyberiantiger.slud.util.ByteBufferInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetOption.TOPT_GMCP;

public class GmcpOptionHandler extends AbstractOptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GmcpOptionHandler.class);
    private final ObjectMapper mapper;

    @Inject
    public GmcpOptionHandler(Lazy<TelnetSocketChannelHandler> handler, ObjectMapper mapper) {
        super(handler, TOPT_GMCP, true, true, true, true);
        this.mapper = mapper;
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(40);
        data.mark();
        while (data.hasRemaining()) {
            byte ch = data.get();
            if (ch == (byte) ' ') {
                break;
            }
            buffer.write(ch);
        }
        byte[] gmcpTypeData = buffer.toByteArray();
        String gmcpType = new String(gmcpTypeData, 0, gmcpTypeData.length);
        GmcpTypeHandler<?> handler = GmcpTypeHandlers.INSTANCE.getGmcpTypeHandler(gmcpType);
        try {
            if (handler != null) {
                handler.handle(mapper.readValue(new ByteBufferInputStream(data), handler.getJavaType()));
            } else {
                log.info("Unhandled GCMP data: {} {}", gmcpType, mapper.readValue(new ByteBufferInputStream(data), JsonNode.class));
            }
        } catch (IOException ex) {
            log.error("Error parsing GMCP data for {}", gmcpType, ex);
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
