package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import static org.cyberiantiger.slud.model.SkillGroup.*;

public enum Skill {
    @JsonProperty("attack")
    ATTACK(COMBAT, "Attack"),
    @JsonProperty("melee")
    MELEE(COMBAT, "Melee"),
    @JsonProperty("defence")
    DEFENCE(COMBAT, "Defence"),
    @JsonProperty("armoury")
    ARMOURY(COMBAT, "Armoury"),
    @JsonProperty("double wielding")
    DOUBLE_WIELDING(COMBAT, "Double Wielding"),
    @JsonProperty("shields")
    SHIELDS(COMBAT, "Shields"),
    @JsonProperty("knife")
    KNIFE(WEAPONS, "Knife"),
    @JsonProperty("blade")
    BLADE(WEAPONS, "Blade"),
    @JsonProperty("blunt")
    BLUNT(WEAPONS, "Blunt"),
    @JsonProperty("projectile")
    PROJECTILE(WEAPONS, "Projectile"),
    @JsonProperty("two handed")
    TWO_HANDED(WEAPONS, "Two Handed"),
    @JsonProperty("polearm")
    POLEARM(WEAPONS, "Polearm"),
    @JsonProperty("staff")
    STAFF(WEAPONS, "Staff"),
    @JsonProperty("whip")
    WHIP(WEAPONS, "Whip"),
    @JsonProperty("thrown")
    THROWN(WEAPONS, "Thrown"),
    @JsonProperty("healing")
    HEALING(MAGIC, "Healing"),
    @JsonProperty("faith")
    FAITH(MAGIC, "Faith"),
    @JsonProperty("magic attack")
    MAGIC_ATTACK(MAGIC, "Magic Attack"),
    @JsonProperty("magic defence")
    MAGIC_DEFENCE(MAGIC, "Magic Defence"),
    @JsonProperty("conjuring")
    CONJURING(MAGIC, "Conjuring"),
    @JsonProperty("woodcraft")
    WOODCRAFT(MAGIC, "Woodcraft"),
    @JsonProperty("insight")
    INSIGHT(MAGIC, "Insight"),
    @JsonProperty("bargaining")
    BARGAINING(OTHER, "Bargaining"),
    @JsonProperty("stealing")
    STEALING(OTHER, "Stealing"),
    @JsonProperty("murder")
    MURDER(OTHER, "Murder"),
    @JsonProperty("skullduggery")
    SKULLDUGGERY(OTHER, "Skullduggery"),
    @JsonProperty("chi")
    CHI(OTHER, "Chi"),
    @JsonProperty("stealth")
    STEALTH(OTHER, "Stealth"),
    @JsonProperty("riding")
    RIDING(OTHER, "Riding"),
    @JsonProperty("application")
    APPLICATION(OTHER, "Application");

    @Getter
    private final SkillGroup skillGroup;

    @Getter
    private final String displayName;

    Skill(SkillGroup skillGroup, String displayName) {
        this.skillGroup = skillGroup;
        this.displayName = displayName;
    }
}
