package org.cyberiantiger.slud.ui.component;

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
    private final ImageIcon baseIcon;
    private final ImageIcon gaugeIcon;
    private final ImageIcon overlayIcon;
    private final RescaleOp gaugeColor;
    private final RescaleOp changeColor;
    private final int minPx;
    private final int maxPx;
    private final boolean horizontal;

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

    private BufferedImage base;
    private BufferedImage gauge;
    private BufferedImage overlay;
    private Timer swingTimer = new Timer(1000 / FPS, this::animate);

    public SkinnableGauge(ImageIcon base,
                          ImageIcon gauge,
                          ImageIcon overlay,
                          Color gaugeColor,
                          Color changeColor,
                          int minPx,
                          int maxPx,
                          boolean horizontal) {
        this.baseIcon = base;
        this.gaugeIcon = gauge;
        this.overlayIcon = overlay;
        this.gaugeColor = rescaleOpFromColor(gaugeColor);
        this.changeColor = rescaleOpFromColor(changeColor);
        this.minPx = minPx;
        this.maxPx = maxPx;
        this.horizontal = horizontal;
        Dimension size = new Dimension(base.getIconWidth(), base.getIconHeight());
        setBackground(Color.BLACK);
        setOpaque(false);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    public void setValue(float percentValue, String text) {
        if (percentValue < 0f) {
            percentValue = 0f;
        } else if (percentValue > 1f) {
            percentValue = 1f;
        }
        this.text = text;
        this.percentValue = percentValue;
        changeVelocity = (percentValue - delayedValue) / (FPS * 2);
        if (!swingTimer.isRunning()) {
            swingTimer.restart();
        }
        repaint();
    }

    private void initOffscreen() {
        if (base == null) {
            base = toBufferedImage(baseIcon);
            gauge = toBufferedImage(gaugeIcon);
            overlay = toBufferedImage(overlayIcon);
        }
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
        initOffscreen();
        Graphics2D g = (Graphics2D) gg;

        int delayedPx = getDelayedPx();
        int valuePx = getValuePx();

        Shape clip = g.getClip(); // Save current clip.

        // Draw base image.
        g.drawImage(base, 0, 0, null);

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

        FontMetrics metrics = g.getFontMetrics();
        int x = (base.getWidth() - metrics.stringWidth(text)) / 2;
        int y = ((base.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y); // TODO: must be inside bounds of our images or it'll corrupt the display.
    }

    private void clip(Graphics2D g, int min, int max) {
        if (horizontal) {
            g.clipRect(min, 0, max - min, base.getHeight());
        } else {
            g.clipRect(0, min, base.getWidth(), max - min);
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

    private BufferedImage toBufferedImage(ImageIcon icon) {
        BufferedImage result = getGraphicsConfiguration()
                .createCompatibleImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        // paint the Icon to the BufferedImage.
        icon.paintIcon(this, g, 0,0);
        g.dispose();
        return result;
    }

    private static RescaleOp rescaleOpFromColor(Color color) {
            return new RescaleOp(
                    new float[] {color.getRed() / 256f, color.getGreen() / 256f, color.getBlue() / 256f, 1f},
                    new float[] {0f, 0f, 0f, 0f},
                    null);
    }

    /*
    public static void main(String... args) throws InterruptedException {
        JFrame frame = new JFrame();
        SkinnableGauge gauge = new SkinnableGauge(
                GAUGE_BASE.load(),
                GAUGE_GAUGE.load(),
                GAUGE_OVERLAY.load(),
                Color.RED,
                Color.RED.darker().darker(),
                8, 248, true);
        gauge.setForeground(Color.WHITE);
        frame.getRootPane().setLayout(new BorderLayout());
        frame.getRootPane().add(gauge, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);

        while (true) {
            sleep(4000);
            gauge.setValue(0.5f, "Hello");
            sleep(4000);
            gauge.setValue(0.4f, "World");
            sleep(4000);
            gauge.setValue(0.6f, "Test");
        }
    }
    */
}
