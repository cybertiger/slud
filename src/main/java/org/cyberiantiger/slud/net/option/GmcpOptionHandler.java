package org.cyberiantiger.slud.net.option;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Lazy;
import org.apache.commons.io.IOUtils;
import org.cyberiantiger.slud.model.GmcpHandler;
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
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.cyberiantiger.slud.net.TelnetOption.TOPT_GMCP;

public class GmcpOptionHandler extends AbstractOptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GmcpOptionHandler.class);
    private final ObjectMapper mapper;
    private final GmcpTypeHandlers typeHandlers;

    @Inject
    public GmcpOptionHandler(Lazy<TelnetSocketChannelHandler> handler,
                             ObjectMapper mapper,
                             GmcpTypeHandlers typeHandlers) {
        super(handler, TOPT_GMCP, true, true, true, true);
        this.mapper = mapper;
        this.typeHandlers = typeHandlers;
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
        ByteBuffer dataCopy = data.slice();

        try {
            Consumer<GmcpHandler> action = typeHandlers.handle(gmcpType, new ByteBufferInputStream(data));
            if (action == null) {
                log.warn("Unhandled GMCP message: {} {}", gmcpType,
                        IOUtils.toString(new ByteBufferInputStream(dataCopy), UTF_8));
            } else {
                getHandler().addUiAction(action);
            }
        } catch (IOException ex) {
            try {
                log.error("Error parsing GMCP data for {} {}",
                        gmcpType,
                        IOUtils.toString(new ByteBufferInputStream(dataCopy), UTF_8),
                        ex);
            } catch (IOException ex2) {
                throw new UncheckedIOException(ex2);
            }
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
            out.write(type.getBytes(UTF_8));
            out.write(0x20); // Space.
            mapper.writeValue(out, data);
            sendSuboption(ByteBuffer.wrap(out.toByteArray()));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
