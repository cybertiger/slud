package org.cyberiantiger.slud.ui;

import lombok.Getter;

import javax.swing.*;

public enum IconType {
    CONNECTED("connected.png"),
    CONNECTING("connecting.gif"), // Animated.
    DISCONNECTED("disconnected.png"),
    GAUGE_BASE("gauge_base.png"),
    GAUGE_GAUGE("gauge_gauge.png"),
    GAUGE_OVERLAY("gauge_overlay.png");

    @Getter
    private String resource;

    IconType(String resource) {
        this.resource = resource;
    }

    public ImageIcon load() {
        return new ImageIcon(SludUi.class.getResource(resource));
    }
}
