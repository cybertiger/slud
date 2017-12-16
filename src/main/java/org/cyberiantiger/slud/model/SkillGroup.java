package org.cyberiantiger.slud.model;

import lombok.Getter;

public enum SkillGroup {
    COMBAT("Combat"),
    WEAPONS("Weapons"),
    MAGIC("Magic"),
    OTHER("Other");

    @Getter
    private String displayName;

    SkillGroup(String displayName) {
        this.displayName = displayName;
    }
}
