package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Item subtypes.
 *
 * Note, subtypes don't include a reference to their type, since that is sent by the mud, also
 * they'll probably be some subtypes that belong in more than one category.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ItemSubtype {
    @JsonProperty("0")
    NONE("None"), // Abuse the fact that jackson deserializes 0 to the zeroth element in the array.
    // Armor types
    @JsonProperty("helmet")
    HELMET("Helmet"),
    @JsonProperty("gloves")
    GLOVES("Gloves"),
    @JsonProperty("amulet")
    AMULET("Amulet"),
    @JsonProperty("cloak")
    CLOAK("Cloak"),
    @JsonProperty("armour")
    ARMOR("Armour"),
    @JsonProperty("ring")
    RING("Ring"),
    @JsonProperty("boots")
    BOOTS("Boots"),
    // Weapon types.
    @JsonProperty("knife")
    KNIFE("Knife"),
    @JsonProperty("blade")
    BLADE("Blade"),
    @JsonProperty("blunt")
    BLUNT("Blunt"),
    @JsonProperty("projectile")
    PROJECTILE("Projectile"),
    @JsonProperty("two handed")
    TWO_HANDED("Two Handed"),
    @JsonProperty("polearm")
    POLEARM("Polearm"),
    @JsonProperty("staff")
    STAFF("Staff"),
    @JsonProperty("whip")
    WHIP("Whip"),
    @JsonProperty("thrown")
    THROWN("Thrown");

    private String displayName;
}
