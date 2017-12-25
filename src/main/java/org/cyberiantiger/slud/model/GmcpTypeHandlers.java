package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Singleton
public class GmcpTypeHandlers {
    private static final Logger log = LoggerFactory.getLogger(GmcpTypeHandlers.class);

    private final ObjectMapper mapper;
    private final Map<String, GmcpTypeHandler> handlerMap = new HashMap<>();

    @Inject
    GmcpTypeHandlers(ObjectMapper mapper) {
        this.mapper = mapper;
        for (Method m : GmcpHandler.class.getMethods()) {
            if (m.isAnnotationPresent(GmcpMessage.class)) {
                GmcpMessage annotation = m.getAnnotation(GmcpMessage.class);
                Type[] arguments = m.getGenericParameterTypes();
                if (arguments.length != 1) {
                    throw new IllegalStateException("Expected method " + m.getName() + " to have a single argument");
                }
                Type argument = arguments[0];
                JavaType javaType = TypeFactory.defaultInstance().constructType(argument);
                handlerMap.put(annotation.value(), new GmcpTypeHandler(m, javaType));
            }
        }
    }

    public Consumer<GmcpHandler> handle(String type, InputStream data) throws IOException {
        GmcpTypeHandler typeHandler = handlerMap.get(type);
        if (typeHandler == null) {
            return null;
        } else {
            return typeHandler.getConsumer(data);
        }
    }

    private final class GmcpTypeHandler {
        private final Method m;
        private final JavaType type;

        private GmcpTypeHandler(Method m, JavaType type) {
            this.m = m;
            this.type = type;
        }

        public Consumer<GmcpHandler> getConsumer(InputStream data) throws IOException {
            Object parsedData = mapper.readValue(data, type);
            return handler -> {
                try {
                    m.invoke(handler, parsedData);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Could not invoke GmcpHandler method on " + m.getClass().getName());
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException(e.getCause());
                }
            };
        }
    }
}
