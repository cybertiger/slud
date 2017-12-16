package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharStatus {
    private String name;
    private String fullname;
    private Race race;
    private Gender gender;
    private int level;
    // TODO: Handle guild "class", possible array of guilds, currently String.
}
