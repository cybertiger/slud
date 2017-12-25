package org.cyberiantiger.slud.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Experience {
    private long value;
    private long min;
    private long max;

    public Experience() {
        reset();
    }

    public void reset() {
        this.value = 0;
        this.min = 0;
        this.max = 0;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void setMax(long max) {
        this.max = max;
    }

    /*
    private void updateGauge() {
        if (max == 0) {
            gauge.setValue(0, "");
            return;
        }
        // TODO: Something cleverer with xp values based off character level.
        // If we know current level min, and next level min we can do nicer things.
        if (value <= max) {
            float percent = 1f * (value - min) / (max - min);
            // Show percent of level.
            gauge.setValue(percent, String.format("%.2f%%", percent * 100));
        } else {
            float percent = 1f - (1f * max / value);
            // Show percent xp buffer over next level (?!)
            gauge.setValue(percent, String.format("%.2f%% (buffer)", percent * 100));
        }
    }
    */

    public String toString() {
        return "" + value + "/(" + min + ':' + max + ')';
    }
}
