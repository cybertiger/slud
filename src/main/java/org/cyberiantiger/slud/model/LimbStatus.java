package org.cyberiantiger.slud.model;

import lombok.Value;

@Value
public class LimbStatus {
    int hp;
    // Optional, may not be sent when max hp does not change from previous message.
    Integer maxhp;
    Boolean bandaged;
    Boolean broken;
    Boolean severed;
}
