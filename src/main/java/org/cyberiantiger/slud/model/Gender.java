package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum Gender {
    @JsonProperty("male")
    MALE("Male"),
    @JsonProperty("female")
    FEMALE("Female"),
    @JsonProperty("neuter")
    NEUTER("Neuter");

    @Getter
    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }
}
