package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;
import org.cyberiantiger.slud.json.IntBooleanConverter;

@Value
public class LimbStatus {
    int hp;
    // Optional, may not be sent when max hp does not change from previous message.
    Integer maxhp;
    @JsonDeserialize(contentConverter = IntBooleanConverter.class)
    Boolean bandaged;
    @JsonDeserialize(contentConverter = IntBooleanConverter.class)
    Boolean broken;
    @JsonDeserialize(contentConverter = IntBooleanConverter.class)
    Boolean severed;

    public boolean isBandaged() {
        return bandaged != null && bandaged;
    }

    public boolean isBroken() {
        return broken != null && broken;
    }

    public boolean isSevered() {
        return severed != null && severed;
    }
}
