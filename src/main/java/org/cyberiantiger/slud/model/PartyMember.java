package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class PartyMember {
    Race race;
    Gender gender;
    @JsonProperty("class")
    List<CharacterClass> characterClass;
    boolean dead;
    boolean netdead;
    int level;
}
