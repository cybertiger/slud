package org.cyberiantiger.slud.ui.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import static java.lang.Thread.sleep;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.cyberiantiger.slud.ui.IconType.*;


/**
 * Horrible UI code.
 *
 * TODO: split up into model and UI delegate.
 */
public class SkinnableGauge extends JComponent {
    private static final Logger log = LoggerFactory.getLogger(SkinnableGauge.class);
    private static final int FPS = 10; // frames per second.
    private final ImageIcon baseIcon;
    private final ImageIcon gaugeIcon;
    private final ImageIcon overlayIcon;
    private final RescaleOp gaugeColor;
    private final RescaleOp positiveChangeColor;
    private final RescaleOp negativeChangeColor;
    private final int minPx;
    private final int maxPx;
    private final boolean horizontal;

    private int max;
    private int value;
    private float changeValue;
    private float changeUpdateVelocity;

    private BufferedImage base;
    private BufferedImage gauge;
    private BufferedImage overlay;
    private BufferedImage render;
    private Graphics2D rg;
    private Timer swingTimer = new Timer(1000 / FPS, this::animate);

    public SkinnableGauge(ImageIcon base,
                          ImageIcon gauge,
                          ImageIcon overlay,
                          Color gaugeColor,
                          Color positiveChangeColor,
                          Color negativeChangeColor,
                          int minPx,
                          int maxPx,
                          boolean horizontal) {
        this.baseIcon = base;
        this.gaugeIcon = gauge;
        this.overlayIcon = overlay;
        this.gaugeColor = rescaleOpFromColor(gaugeColor);
        this.positiveChangeColor = rescaleOpFromColor(positiveChangeColor);
        this.negativeChangeColor = rescaleOpFromColor(negativeChangeColor);
        this.minPx = minPx;
        this.maxPx = maxPx;
        this.horizontal = horizontal;
        this.max = 0;
        this.value = 0;
        this.changeValue = 0f;
        this.changeUpdateVelocity = 0f;
        Dimension size = new Dimension(base.getIconWidth(), base.getIconHeight());
        setBackground(Color.BLACK);
        setOpaque(false);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        swingTimer.start();
    }

    private void initOffscreen() {
        if (render == null) {
            base = toBufferedImage(baseIcon);
            gauge = toBufferedImage(gaugeIcon);
            overlay = toBufferedImage(overlayIcon);
            render = getGraphicsConfiguration()
                    .createCompatibleImage(baseIcon.getIconWidth(), baseIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            rg = render.createGraphics();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        initOffscreen();
        // clear to transparent.
        rg.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        rg.fillRect(0,0, render.getWidth(), render.getHeight());

        //reset composite
        rg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        // Draw base.
        rg.drawImage(base, 0, 0, null);

        // Draw gauge with color.
        clip(0, value);
        rg.drawImage(gauge, gaugeColor, 0, 0);

        int changeValue = (int) (this.changeValue < 0 ? Math.ceil(this.changeValue) : Math.floor(this.changeValue));
        if (Math.abs(changeValue) != 0) {
            // TODO: set clip rectangle.
            if (changeValue < 0) {
                clip(value, value - changeValue);
                rg.drawImage(gauge, negativeChangeColor, 0, 0);
            } else {
                clip(value - changeValue, value);
                rg.drawImage(gauge, positiveChangeColor, 0, 0);
            }
        }
        // Reset clip area.
        rg.setClip(0, 0, render.getWidth(), render.getHeight());

        rg.drawImage(overlay, 0, 0, null);

        // Render offscreen image.
        g.drawImage(render, 0, 0, null);
    }

    private void clip(int min, int max) {
        double minPercent = this.max <= 0 ? 1d : 1d * min / this.max;
        double maxPercent = this.max <= 0 ? 1d : 1d * max / this.max;
        if (maxPercent < 0d) {
            maxPercent = 0d;
        } else if (maxPercent > 1d) {
            maxPercent = 1d;
        }
        if (minPercent < 0d) {
            minPercent = 0d;
        } else if (minPercent > 1d) {
            minPercent = 1d;
        }
        if (maxPercent < minPercent) {
            minPercent = maxPercent;
        }
        int maxClip = (int) (minPx + (maxPx - minPx) * maxPercent);
        int minClip = (int) (minPx + (maxPx - minPx) * minPercent);

        if (horizontal) {
            rg.setClip(minClip, 0, maxClip - minClip, render.getHeight());
        } else {
            rg.setClip(0, minClip, render.getWidth(), maxClip - minClip);
        }
    }

    private void animate(ActionEvent event) {
        float oldValue = changeValue;
        changeValue -= changeUpdateVelocity;
        if ( (changeValue <= 0 & oldValue >= 0) ||
                (changeValue >= 0 & oldValue <= 0)) {
            changeValue = 0f;
            changeUpdateVelocity = 0f;
            swingTimer.stop();
        }
        repaint();
    }

    public void setMax(int max) {
        this.max = max;
        repaint();
    }

    public void setValue(int value) {
        this.changeValue += value - this.value;
        this.changeUpdateVelocity = changeValue / ( FPS * 2 ); /* 2 second animation, conveniently 1 mud tick */
        this.value = value;
        if (changeValue != 0f) {
            swingTimer.restart();
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
                Color.GREEN,
                Color.BLUE,
                8, 292, true);
        gauge.max = 1000;
        gauge.value = 500;
        frame.getRootPane().setLayout(new BorderLayout());
        frame.getRootPane().add(gauge, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);

        while (true) {
            sleep(4000);
            gauge.setValue(500);
            sleep(4000);
            gauge.setValue(400);
            sleep(4000);
            gauge.setValue(600);
        }
    }
    */
}
