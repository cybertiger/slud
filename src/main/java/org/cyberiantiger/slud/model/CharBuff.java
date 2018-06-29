package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Value;

import java.io.IOException;

@Value
@JsonDeserialize(using = CharBuff.CharBuffDeserializer.class)
public class CharBuff {
    int duration;
    String description;
    boolean debuff;
    int stackCount;

    /**
     * Custom deserializer.
     *
     * I could convince Jackson to deserialize arrays as objects, but I couldn't convince it
     * to deserialize arrays or zero as objects or null. :(
     */
    public static class CharBuffDeserializer extends StdDeserializer<CharBuff> {
        public CharBuffDeserializer() {
            super(CharBuff.class);
        }

        public CharBuff deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
            switch (jp.currentToken()) {
                case VALUE_NUMBER_INT:
                    jp.nextToken();
                    return null;
                case START_ARRAY:
                    if (jp.nextToken() != JsonToken.VALUE_NUMBER_INT) {
                        throw new IOException("Expecting integer");
                    }
                    int duration = jp.getValueAsInt();
                    if (jp.nextToken() != JsonToken.VALUE_STRING) {
                        throw new IOException("Expecting string");
                    }
                    String description = jp.getValueAsString();
                    if (jp.nextToken() != JsonToken.VALUE_NUMBER_INT) {
                        throw new IOException("Expecting integer");
                    }
                    boolean debuff = !((Integer)0).equals(jp.getValueAsInt());
                    if (jp.nextToken() != JsonToken.VALUE_NUMBER_INT) {
                        throw new IOException("Expecting integer");
                    }
                    int stackCount = jp.getValueAsInt();
                    if (jp.nextToken() != JsonToken.END_ARRAY) {
                        throw new IOException("Expecting end of array");
                    }
                    jp.nextToken();
                    return new CharBuff(duration, description, debuff, stackCount);
                default:
                    throw new IOException("Expected START_ARRAY or NUMBER_INT for Charbuff");
            }
        }
    }
}
