package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
public class TellMessage {
    public enum TellType {
        TELL("tell"),
        REPLY_TO("reply to");

        private final String jsonValue;

        TellType(String jsonValue) {
            this.jsonValue = jsonValue;
        }

        @JsonCreator
        public static TellType fromJson(String jsonValue) {
            for (TellType type : TellType.values()) {
                if (type.jsonValue.equals(jsonValue)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown TellType: " + jsonValue);
        }

        @JsonValue
        public String jsonValue() {
            return jsonValue;
        }
    }

    TellType type;
    String rawmsg;
    String msg;
    String to;
    String from;
}
