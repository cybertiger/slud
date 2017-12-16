package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * List of races.
 *
 * TODO: stat and skill mods.
 * TODO: limbs.
 */
public enum Race {
    @JsonProperty("dwarf")
    DWARF("Dwarf"),
    @JsonProperty("elf")
    ELF("Elf"),
    @JsonProperty("faerie")
    FAERIE("Faerie"),
    @JsonProperty("giant")
    GIANT("Giant"),
    @JsonProperty("gnome")
    GNOME("Gnome"),
    @JsonProperty("half-elf")
    HALF_ELF("Half-Elf"),
    @JsonProperty("human")
    HUMAN("Human"),
    @JsonProperty("nymph")
    NYMPH("Nymph"),
    @JsonProperty("satyr")
    SATYR("Satyr"),
    @JsonProperty("half-orc")
    HALF_ORC("half-orc"),
    @JsonProperty("halfling")
    HALFLING("Halfling"),
    @JsonProperty("artrell")
    ARTRELL("Artrell");

    @Getter
    private final String displayName;

    Race(String displayName) {
        this.displayName = displayName;
    }
}
