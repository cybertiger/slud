package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ItemType {
    @JsonProperty("0")
    NONE("None"), // Abuse the fact that jackson deserializes 0 to the zeroth element in the array.
    @JsonProperty("armour")
    ARMOR("Armour"),
    @JsonProperty("weapon")
    WEAPON("Weapon"),
    @JsonProperty("food")
    FOOD("Food"),
    @JsonProperty("drink")
    DRINK("Drink");

    @Getter
    private final String displayName;
}
