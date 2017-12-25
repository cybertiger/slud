package org.cyberiantiger.slud.ui.model;

import lombok.Data;

@Data
public class LimbData {
    private int hp;
    private int maxhp;
    private boolean broken;
    private boolean severed;
    private boolean bandaged;

    public String toString() {
        return "" + hp + "/" + maxhp
                + (broken ? " (broken)" : "")
                + (severed ? " (severed)" : "")
                + (bandaged ? " (bandaged)" : "");
    }
}
