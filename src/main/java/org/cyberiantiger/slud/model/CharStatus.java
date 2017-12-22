package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class CharStatus {
    private String name;
    private String fullname;
    private Race race;
    private Gender gender;
    private Integer level;
    @JsonProperty("class")
    private List<CharacterClass> characterClass;
}
