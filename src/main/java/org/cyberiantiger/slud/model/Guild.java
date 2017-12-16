package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum Guild {
    @JsonProperty("child")
    CHILD("Child"),
    @JsonProperty("fighter")
    FIGHTER("Fighter"),
    @JsonProperty("monk")
    MONK("Monk"),
    @JsonProperty("warmage")
    WARMAGE("Warmage"),
    @JsonProperty("cleric")
    CLERIC("Cleric"),
    @JsonProperty("rogue")
    ROGUE("Rogue"),
    @JsonProperty("druid")
    DRUID("Druid"),
    @JsonProperty("ranger")
    RANGER("Ranger"),
    @JsonProperty("necromancer")
    NECROMANCER("Necromancer"); // Never hurts to be hopeful.

    @Getter
    private final String displayName;

    Guild(String displayName) {
        this.displayName = displayName;
    }
}
