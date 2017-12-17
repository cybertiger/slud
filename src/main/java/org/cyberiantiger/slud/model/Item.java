package org.cyberiantiger.slud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

@Value
public class Item {
    String name;
    ItemType type;
    ItemSubtype subtype;

    // Map zero to null.
    @JsonCreator
    public static Item createNull(int value) {
        return null;
    }
}
