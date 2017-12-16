package org.cyberiantiger.slud.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cyberiantiger.slud.ui.SludUi;
import org.cyberiantiger.slud.ui.component.SkinnableGauge;

import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: Try to decouple model / UI impl.
@Data
@Singleton
public class Avatar {
    private final SludUi ui;
    private final Vital hp;
    private final Vital mp;
    private final Vital sp;
    private final Experience xp;

    @Inject
    public Avatar(SludUi ui) {
        this.ui = ui;
        hp = new Vital(ui.getHpBar(), 0, 0);
        mp = new Vital(ui.getMpBar(), 0, 0);
        sp = new Vital(ui.getSpBar(), 0, 0);
        xp = new Experience(ui.getXpBar(), 0, 0);
    }

    public void reset() {
        hp.reset();
        mp.reset();
        sp.reset();
        xp.reset();
    }

    public void quit() {
        reset();
    }

    @Data
    @AllArgsConstructor
    public static class Vital {
        private final SkinnableGauge gauge;
        private int value;
        private int max;

        public void reset() {
            this.value = 0;
            this.max = 0;
            updateGauge();
        }

        public void setValue(int value) {
            this.value = value;
            updateGauge();
        }

        public void setMax(int max) {
            this.max = max;
            updateGauge();
        }

        private void updateGauge() {
            if (max == 0) {
                gauge.setValue(0, "");
            } else {
                gauge.setValue(1f * value / max, String.format("%d / %d", value, max));
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class Experience {
        private final SkinnableGauge gauge;
        private long value;
        private long max;

        public void reset() {
            this.value = 0;
            this.max = 0;
            updateGauge();
        }

        public void setValue(long value) {
            this.value = value;
            updateGauge();
        }

        public void setMax(long max) {
            this.max = max;
            updateGauge();
        }

        private void updateGauge() {
            if (max == 0) {
                gauge.setValue(0, "");
                return;
            }
            // TODO: Something cleverer with xp values based off character level.
            // If we know current level min, and next level min we can do nicer things.
            if (value <= max) {
                float percent = 1f * value / max;
                // Show percent of level.
                gauge.setValue(percent, String.format("%.2f%%", percent * 100));
            } else {
                float percent = 1f - (1f * max / value);
                // Show percent xp buffer.
                gauge.setValue(percent, String.format("%.2f%% (buffer)", percent * 100));
            }
        }
    }
}
