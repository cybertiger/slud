package org.cyberiantiger.slud.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vital {
    private int value;
    private int max;

    public Vital() {
    }

    public void reset() {
        this.value = 0;
        this.max = 0;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /*
    private void updateGauge() {
        if (max == 0) {
            gauge.setValue(0, "");
        } else {
            gauge.setValue(1f * value / max, String.format("%d / %d", value, max));
        }
    }
    */

    public String toString() {
        return "" + value + '/' + max;
    }
}
