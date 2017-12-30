package org.cyberiantiger.slud.ui;

import lombok.Getter;
import org.cyberiantiger.slud.model.CharacterClass;
import org.cyberiantiger.slud.model.Gender;
import org.cyberiantiger.slud.model.Race;

import javax.swing.*;
import java.util.function.Supplier;

public enum IconType implements Supplier<ImageIcon> {
    CONNECTED("connected.png"),
    CONNECTING("connecting.gif"), // Animated.
    DISCONNECTED("disconnected.png"),
    GAUGE_BASE("gauge_base.png"),
    GAUGE_GAUGE("gauge_gauge.png"),
    GAUGE_OVERLAY("gauge_overlay.png"),
    GAUGE_BASE_SMALL("gauge_base_small.png"),
    GAUGE_GAUGE_SMALL("gauge_gauge_small.png"),
    GAUGE_OVERLAY_SMALL("gauge_overlay_small.png"),
    DWARF_64("dwarf64.png"),
    ELF_64("elf64.png"),
    FAERIE_64("faerie64.png"),
    GIANT_64("giant64.png"),
    GNOME_64("gnome64.png"),
    HALF_ELF_64("half_elf64.png"),
    HUMAN_64("human64.png"),
    NYMPH_64("nymph64.png"),
    SATYR_64("satyr64.png"),
    HALF_ORC_64("half_orc64.png"),
    HALFLING_64("halfling64.png"),
    ARTRELL_64("artrell64.png"),
    HAWK_64("hawk64.png"),
    BEAR_64("bear64.png"),
    WOLF_64("wolf64.png"),
    DOLPHIN_64("dolphin64.png"),
    DIRE_WOLF_64("dire_wolf64.png"),
    GRIZZLY_BEAR_64("grizzly_bear64.png"),
    CHILD_32("child32.png"),
    FIGHTER_32("fighter32.png"),
    MONK_32("monk32.png"),
    WARMAGE_32("warmage32.png"),
    CLERIC_32("cleric32.png"),
    ROGUE_32("rogue32.png"),
    DRUID_32("druid32.png"),
    RANGER_32("ranger32.png"),
    NECROMANCER_32("necromancer32.png"), // Never hurts to be hopeful.
    MALE_32("male32.png"),
    FEMALE_32("female32.png"),
    NEUTER_32("neuter32.png");

    @Getter
    private String resource;
    private ImageIcon icon;

    IconType(String resource) {
        this.resource = resource;
        this.icon = new ImageIcon(IconType.class.getResource(resource));
    }

    public static IconType getRace64(Race race) {
        return values()[DWARF_64.ordinal() + race.ordinal()];
    }

    public static IconType getCharacterClass32(CharacterClass cclass) {
        return values()[CHILD_32.ordinal() + cclass.ordinal()];
    }

    public static IconType getGender32(Gender gender) {
        return values()[MALE_32.ordinal() + gender.ordinal()];
    }

    public ImageIcon get() {
        return icon;
    }
}
