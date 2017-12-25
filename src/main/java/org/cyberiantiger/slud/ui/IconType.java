package org.cyberiantiger.slud.ui;

import lombok.Getter;

import javax.swing.*;
import java.util.function.Supplier;

public enum IconType implements Supplier<ImageIcon> {
    CONNECTED("connected.png"),
    CONNECTING("connecting.gif"), // Animated.
    DISCONNECTED("disconnected.png"),
    GAUGE_BASE("gauge_base.png"),
    GAUGE_GAUGE("gauge_gauge.png"),
    GAUGE_OVERLAY("gauge_overlay.png");

    @Getter
    private String resource;
    private ImageIcon icon;

    IconType(String resource) {
        this.resource = resource;
        this.icon = new ImageIcon(IconType.class.getResource(resource));
    }

    public ImageIcon get() {
        return icon;
    }
}
