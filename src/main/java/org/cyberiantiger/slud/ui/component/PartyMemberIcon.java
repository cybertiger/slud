package org.cyberiantiger.slud.ui.component;

import lombok.Getter;
import org.cyberiantiger.slud.model.CharacterClass;
import org.cyberiantiger.slud.model.Gender;
import org.cyberiantiger.slud.model.Race;
import org.cyberiantiger.slud.ui.IconType;
import org.cyberiantiger.slud.ui.ImageCache;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class PartyMemberIcon extends JComponent {

    private ImageCache cache;
    @Getter
    private Race race;
    @Getter
    private Gender gender;
    @Getter
    private CharacterClass primary;
    @Getter
    private CharacterClass secondary;

    public PartyMemberIcon(ImageCache cache) {
        this.cache = cache;
        setOpaque(false);
        Dimension size = new Dimension(64, 64);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void setMember(Race race, Gender gender, CharacterClass primary, CharacterClass secondary) {
        this.race = race;
        this.gender = gender;
        this.primary = primary;
        this.secondary = secondary;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        Graphics2D gg = (Graphics2D) g;
        Optional.ofNullable(race).ifPresent(oRace -> gg.drawImage(
                cache.getBufferedImage(gc, IconType.getRace64(oRace)),
                0, 0, null));
        Optional.ofNullable(gender).ifPresent(oGender -> gg.drawImage(
                cache.getBufferedImage(gc, IconType.getGender32(oGender)),
                32, 0, null));
        Optional.ofNullable(primary).ifPresent(oPrimary -> gg.drawImage(
                cache.getBufferedImage(gc, IconType.getCharacterClass32(oPrimary)),
                0, 32, null));
        Optional.ofNullable(secondary).ifPresent(oSecondary -> gg.drawImage(
                cache.getBufferedImage(gc, IconType.getCharacterClass32(oSecondary)),
                32, 32, null));
    }
}
