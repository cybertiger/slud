package org.cyberiantiger.slud.ui.component;

import lombok.Getter;
import org.cyberiantiger.slud.ui.ImageCache;

import javax.swing.*;
import java.awt.*;

import static org.cyberiantiger.slud.ui.component.SkinnableGauge.createSmallGauge;

public class PartyMemberPanel extends JPanel {
    @Getter
    private PartyMemberIcon icon;
    @Getter
    private SkinnableGauge hp;
    @Getter
    private SkinnableGauge mp;
    @Getter
    private SkinnableGauge sp;
    @Getter
    private boolean drawChildren = false;

    public PartyMemberPanel(ImageCache cache) {
        icon = new PartyMemberIcon(cache);
        hp = createSmallGauge(cache, Color.RED);
        mp = createSmallGauge(cache, Color.BLUE);
        sp = createSmallGauge(cache, Color.YELLOW);
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints;
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 3;
        add(icon, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(hp, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(mp, constraints);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(sp, constraints);
    }

    public void setDrawChildren(boolean drawChildren) {
        this.drawChildren = drawChildren;
        repaint();
    }

    @Override
    protected void paintChildren(Graphics g) {
        if (drawChildren) {
            super.paintChildren(g);
        }
    }
}
