package org.cyberiantiger.slud.gmcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Gmcp {
    private static final Logger log = LoggerFactory.getLogger(Gmcp.class);
    private static final TypeReference<Integer> INTEGER_TYPE = new TypeReference<Integer>() {};
    private static final TypeReference<Long> LONG_TYPE = new TypeReference<Long>() {};
    private static final TypeReference<CharStatus> CHAR_STATUS_TYPE = new TypeReference<CharStatus>() {};
    // TODO: Write enums for these..
    private static final TypeReference<Map<String, Integer>> CHAR_STATS_TYPE = new TypeReference<Map<String, Integer>>() {};
    private static final TypeReference<Map<String, Integer>> CHAR_SKILLS_TYPE = new TypeReference<Map<String, Integer>>() {};

    // TODO: Move this initialization elsewhere.
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    Map<String, GmcpHandler<?>> handlerMap = new HashMap<>();

    public Gmcp() {
        // TODO: Handlers.
        handlerMap.put("char.reset", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.hp", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.maxhp", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.mp", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.maxmp", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.sp", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.maxsp", new GmcpHandler<>(INTEGER_TYPE, null));
        handlerMap.put("char.vitals.exp", new GmcpHandler<>(LONG_TYPE, null));
        handlerMap.put("char.vitals.maxexp", new GmcpHandler<>(LONG_TYPE, null));
        handlerMap.put("char.status", new GmcpHandler<>(LONG_TYPE, null));
        handlerMap.put("char.stats", new GmcpHandler<>(LONG_TYPE, null));
        handlerMap.put("char.skills", new GmcpHandler<>(LONG_TYPE, null));
    }

    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class CharStatus {
        String race; /* TODO: convert to enum */
        String gender; /* TODO: convert to enum */
        String name;
        @JsonProperty("class") // cannot call variables class in java.
        String className;
        String fullname;
        int level;
    }

    public <T> void parse(String gmcpString) {
        int i = gmcpString.indexOf(' ');
        if (i < 0 || i >= gmcpString.length() - 1) {
            log.warn("Got GMCP with no value: {}", gmcpString);
            return;
        }
        String gmcpType = gmcpString.substring(0, i).toLowerCase();
        String gmcpValue = gmcpString.substring(i+1);
        GmcpHandler<?> handler = handlerMap.get(gmcpType);
        if (handler == null) {
            log.warn("Missing handler for GMCP type: {}", gmcpType);
            return;
        }
        handler.handle(gmcpValue);
    }

    @Value
    private class GmcpHandler<T> {
        private TypeReference<T> type;
        private Consumer<T> handler;

        public void handle(String value) {
            try {
                handler.accept(mapper.readValue(value, type));
            } catch (IOException ex) {
                log.warn("Error parsing GMCP data", ex);
            }
        }
    }
}
