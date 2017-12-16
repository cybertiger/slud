package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum of valid limbs.
 *
 * TODO: For now this only contains humanoid limbs, need to add weird races, and druid shape change limbs.
 */
public enum Limb {
    @JsonProperty("head")
    HEAD("Head"),
    @JsonProperty("torso")
    TORSO("Torso"),
    @JsonProperty("left arm")
    LEFT_ARM("Left arm"),
    @JsonProperty("right arm")
    RIGHT_ARM("Right arm"),
    @JsonProperty("left leg")
    LEFT_LEG("Left leg"),
    @JsonProperty("right leg")
    RIGHT_LEG("Right leg"),
    @JsonProperty("left hand")
    LEFT_HAND("Left hand"),
    @JsonProperty("right hand")
    RIGHT_HAND("Right hand"),
    @JsonProperty("left foot")
    LEFT_FOOT("Left foot"),
    @JsonProperty("right foot")
    RIGHT_FOOT("Right foot");

    @Getter
    private final String displayName;

    Limb(String displayName) {
        this.displayName = displayName;
    }
}
