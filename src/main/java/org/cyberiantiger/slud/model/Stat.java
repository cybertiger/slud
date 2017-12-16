package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum Stat {
    @JsonProperty("strength")
    STRENGTH("Strength"),
    @JsonProperty("intelligence")
    INTELLIGENCE("Intelligence"),
    @JsonProperty("wisdom")
    WISDOM("Wisdom"),
    @JsonProperty("dexterity")
    DEXTERITY("Dexterity"),
    @JsonProperty("constitution")
    CONSTITUTION("Constitution"),
    @JsonProperty("charisma")
    CHARISMA("Charisma");

    @Getter
    private final String displayName;

    Stat(String displayName) {
        this.displayName = displayName;
    }
}
