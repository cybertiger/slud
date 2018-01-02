package org.cyberiantiger.slud.ui.component;

import org.cyberiantiger.slud.ui.IconType;
import org.cyberiantiger.slud.ui.ImageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;


/**
 * Horrible UI code.
 *
 * TODO: split up into model and UI delegate.
 */
public class SkinnableGauge extends JComponent {
    private static final Logger log = LoggerFactory.getLogger(SkinnableGauge.class);
    private static final int FPS = 25; // frames per second.
    private final ImageCache cache;
    private final IconType base;
    private final IconType gauge;
    private final IconType overlay;
    private final RescaleOp gaugeColor;
    private final RescaleOp changeColor;
    private final int minPx;
    private final int maxPx;
    private final boolean horizontal;
    private final Dimension size;

    /**
     * Target for the gauge to eventually show.
     */
    private float percentValue = 0f;
    /**
     * Value currently showing.
     */
    private float delayedValue = 0f;
    /**
     * Change to delayed value per animation step.
     */
    private float changeVelocity = 0f;
    /**
     * Text to render.
     */
    private String text = "";

    private Timer swingTimer = new Timer(1000 / FPS, this::animate);

    public static SkinnableGauge createNormalGauge(ImageCache cache, Color color) {
        SkinnableGauge result = new SkinnableGauge(
                cache,
                IconType.GAUGE_BASE,
                IconType.GAUGE_GAUGE,
                IconType.GAUGE_OVERLAY,
                color,
                color.darker().darker(),
                8, 248, true);
        return result;
    }

    public static SkinnableGauge createSmallGauge(ImageCache cache, Color color) {
        SkinnableGauge result = new SkinnableGauge(
                cache,
                IconType.GAUGE_BASE_SMALL,
                IconType.GAUGE_GAUGE_SMALL,
                IconType.GAUGE_OVERLAY_SMALL,
                color,
                color.darker().darker(),
                2, 126, true);
        return result;
    }

    public SkinnableGauge(ImageCache cache,
                          IconType base,
                          IconType gauge,
                          IconType overlay,
                          Color gaugeColor,
                          Color changeColor,
                          int minPx,
                          int maxPx,
                          boolean horizontal) {
        this.cache = cache;
        this.base = base;
        this.gauge = gauge;
        this.overlay = overlay;
        this.gaugeColor = rescaleOpFromColor(gaugeColor);
        this.changeColor = rescaleOpFromColor(changeColor);
        this.minPx = minPx;
        this.maxPx = maxPx;
        this.horizontal = horizontal;
        size = new Dimension(base.get().getIconWidth(), base.get().getIconHeight());
        setBackground(Color.BLACK);
        setOpaque(false);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    public void setValue(float percentValue) {
        if (percentValue < 0f) {
            percentValue = 0f;
        } else if (percentValue > 1f) {
            percentValue = 1f;
        }
        this.percentValue = percentValue;
        changeVelocity = (percentValue - delayedValue) / (FPS * 2);
        if (!swingTimer.isRunning()) {
            swingTimer.restart();
        }
        repaint();
    }

    private int getValuePx() {
        return minPx + (int) (percentValue * (maxPx - minPx));
    }

    private int getDelayedPx() {
        return minPx + (int) (delayedValue * (maxPx - minPx));
    }

    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;

        int delayedPx = getDelayedPx();
        int valuePx = getValuePx();

        Shape clip = g.getClip(); // Save current clip.

        // Draw base image.
        g.drawImage(cache.getBufferedImage(getGraphicsConfiguration(), base), 0, 0, null);

        BufferedImage gauge = cache.getBufferedImage(getGraphicsConfiguration(), this.gauge);
        BufferedImage overlay = cache.getBufferedImage(getGraphicsConfiguration(), this.overlay);

        // Render gauge.
        if (delayedPx == valuePx) {
            // No change to show.
            clip(g, minPx, valuePx);
            g.drawImage(gauge, gaugeColor, 0, 0);
        } else if (delayedPx < valuePx) {
            clip(g, minPx, delayedPx);
            g.drawImage(gauge, gaugeColor, 0, 0);
            g.setClip(clip);
            clip(g, delayedPx, valuePx);
            g.drawImage(gauge, changeColor, 0, 0);
        } else {
            clip(g, minPx, valuePx);
            g.drawImage(gauge, gaugeColor, 0, 0);
            g.setClip(clip);
            clip(g, valuePx, delayedPx);
            g.drawImage(gauge, changeColor, 0, 0);
        }

        g.setClip(clip);
        g.drawImage(overlay, 0, 0, null);

        /*
        FontMetrics metrics = g.getFontMetrics();
        int x = (size.width - metrics.stringWidth(text)) / 2;
        int y = ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y); // TODO: must be inside bounds of our images or it'll corrupt the display.
        */
    }

    private void clip(Graphics2D g, int min, int max) {
        if (horizontal) {
            g.clipRect(min, 0, max - min, size.height);
        } else {
            g.clipRect(0, min, size.width, max - min);
        }
    }

    private void animate(ActionEvent event) {
        float oldValue = delayedValue;
        delayedValue += changeVelocity;

        if ( (delayedValue <= percentValue & oldValue >= percentValue) ||
                (delayedValue >= percentValue & oldValue <= percentValue)) {
            delayedValue = percentValue;
            changeVelocity = 0f;
            if (swingTimer.isRunning()) {
                swingTimer.stop();
            }
        }
        repaint();
    }

    private static RescaleOp rescaleOpFromColor(Color color) {
            return new RescaleOp(
                    new float[] {color.getRed() / 256f, color.getGreen() / 256f, color.getBlue() / 256f, 1f},
                    new float[] {0f, 0f, 0f, 0f},
                    null);
    }
}
