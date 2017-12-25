package org.cyberiantiger.slud.ui;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ImageCache {
    private static final AffineTransform IDENTITY = new AffineTransform();
    private static final Logger log = LoggerFactory.getLogger(ImageCache.class);
    private final Object lock = new Object();
    private Map<Pair<GraphicsConfiguration, IconType>, BufferedImage> images = new HashMap<>();

    @Inject
    public ImageCache() {
    }

    public BufferedImage getBufferedImage(GraphicsConfiguration config, IconType type) {
        // Not synchronized, expects to be called on the UI thread....
        BufferedImage result = images.get(Pair.of(config, type));
        if (result != null) {
            return result;
        }
        log.info("Loading IconType {}", type);
        ImageIcon icon = type.get();
        Image image = icon.getImage();
        result = config.createCompatibleImage(icon.getIconWidth(), icon.getIconHeight(), Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D) result.getGraphics();
        g.drawImage(image, IDENTITY, null);
        g.dispose();
        images.put(Pair.of(config, type), result);
        return result;
    }
}
