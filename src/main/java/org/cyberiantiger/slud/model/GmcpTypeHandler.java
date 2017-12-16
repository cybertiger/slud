package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Interface for gmcp type handlers.
 *
 * @param <T> type this handler expects.
 */
public interface GmcpTypeHandler<T> {
    /**
     * Get the java type for this handler.
     * @return a JavaType
     */
    default JavaType getJavaType() {
        // Evil voodoo, unfortunately does not work for lambdas since the compiler does not generate type information
        // for them, even if it's available.
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        return typeFactory.findTypeParameters(typeFactory.constructType(getClass()), GmcpTypeHandler.class)[0];
    }

    /**
     * Handle GMCP data.
     * @param data GMCP data to handle.
     */
    void handle(T data);
}
