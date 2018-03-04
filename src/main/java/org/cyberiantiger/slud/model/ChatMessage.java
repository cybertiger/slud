package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
public class ChatMessage {
    public enum EmoteType {
        CHAT,
        CUSTOM_EMOTE,
        BUILTIN_EMOTE,
        CUSTOM_EMOTE_NO_SPACE;

        @JsonCreator
        public static EmoteType fromValue(int value) {
            if (value < 0 || value >= values().length) {
                throw new IllegalArgumentException("Unknown emote type: " + value);
            }
            return values()[value];
        }

        @JsonValue
        public int toValue() {
            return ordinal();
        }
    }

    EmoteType emote;
    String who;
    String channel;
    String msg;
    String rawmsg;
}
