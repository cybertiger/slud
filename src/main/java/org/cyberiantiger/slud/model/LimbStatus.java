package org.cyberiantiger.slud.model;

import lombok.Value;

@Value
public class LimbStatus {
    int hp;
    int maxhp;
    int bandaged;
    int broken;
    int severed;

    public boolean isBandaged() {
        return bandaged != 0;
    }

    public boolean isBroken() {
        return broken != 0;
    }

    public boolean isSevered() {
        return severed != 0;
    }
}
