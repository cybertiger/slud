package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum of valid limbs.
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
    RIGHT_FOOT("Right foot"),
    @JsonProperty("left wing")
    LEFT_WING("Left wing"),
    @JsonProperty("right wing")
    RIGHT_WING("Right wing"),
    @JsonProperty("left hoof")
    LEFT_HOOF("Left hoof"),
    @JsonProperty("right hoof")
    RIGHT_HOOF("Right hoof"),
    @JsonProperty("second left arm")
    SECOND_LEFT_ARM("Second left arm"),
    @JsonProperty("second right arm")
    SECOND_RIGHT_ARM("Second right arm"),
    @JsonProperty("second left hand")
    SECOND_LEFT_HAND("Second left hand"),
    @JsonProperty("second right hand")
    SECOND_RIGHT_HAND("Second right hand"),
    @JsonProperty("beak")
    BEAK("Beak"),
    @JsonProperty("tail")
    TAIL("Tail"),
    @JsonProperty("left claw")
    LEFT_CLAW("Left claw"),
    @JsonProperty("right claw")
    RIGHT_CLAW("Right claw"),
    @JsonProperty("left foreleg")
    LEFT_FORELEG("Left foreleg"),
    @JsonProperty("right foreleg")
    RIGHT_FORELEG("Right foreleg"),
    @JsonProperty("left rear leg")
    LEFT_REAR_LEG("Left rear leg"),
    @JsonProperty("right rear leg")
    RIGHT_REAR_LEG("Right rear leg"),
    @JsonProperty("left forepaw")
    LEFT_FOREPAW("Left forepaw"),
    @JsonProperty("right forepaw")
    RIGHT_FOREPAW("Right forepaw"),
    @JsonProperty("left rear paw")
    LEFT_REAR_PAW("Left rear paw"),
    @JsonProperty("right rear paw")
    RIGHT_REAR_PAW("Right rear paw");

    @Getter
    private final String displayName;

    Limb(String displayName) {
        this.displayName = displayName;
    }
}
