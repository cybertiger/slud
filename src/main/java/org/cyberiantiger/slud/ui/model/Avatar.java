package org.cyberiantiger.slud.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cyberiantiger.slud.model.*;
import org.cyberiantiger.slud.ui.SludUi;
import org.cyberiantiger.slud.ui.component.SkinnableGauge;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

// TODO: Try to decouple model / UI impl.
@Data
@Singleton
public class Avatar {
    private final SludUi ui;
    private final Vital hp;
    private final Vital mp;
    private final Vital sp;
    private final Experience xp;
    private String name;
    private String fullname;
    private Gender gender;
    private Race race;
    private List<CharacterClass> characterClass;
    private Integer level;
    private final EnumMap<Stat, Integer> stats = new EnumMap<>(Stat.class);
    private final EnumMap<Skill, Integer> skills = new EnumMap<>(Skill.class);
    private final EnumMap<Limb, LimbData> limbs = new EnumMap<>(Limb.class);
    private final Party party = new Party();

    @Inject
    public Avatar(SludUi ui) {
        this.ui = ui;
        hp = new Vital(ui.getHpBar());
        mp = new Vital(ui.getMpBar());
        sp = new Vital(ui.getSpBar());
        xp = new Experience(ui.getXpBar());
        reset();
    }

    public void reset() {
        hp.reset();
        mp.reset();
        sp.reset();
        xp.reset();
        name = null;
        fullname = null;
        characterClass = emptyList();
        race = null;
        gender = null;
        level = null;
        stats.clear();
        skills.clear();
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

        public Vital(SkinnableGauge gauge) {
            this.gauge = gauge;
        }

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

        public String toString() {
            return "" + value + '/' + max;
        }
    }

    @Data
    public static class LimbData {
        private int hp;
        private int maxhp;
        private boolean broken;
        private boolean severed;
        private boolean bandaged;

        public String toString() {
            return "" + hp + "/" + maxhp
                    + (broken ? " (broken)" : "")
                    + (severed ? " (severed)" : "")
                    + (bandaged ? " (bandaged)" : "");
        }
    }

    @Data
    @AllArgsConstructor
    public static class Experience {
        private final SkinnableGauge gauge;
        private long value;
        private long min;
        private long max;

        public Experience(SkinnableGauge gauge) {
            this.gauge = gauge;
            reset();
        }

        public void reset() {
            this.value = 0;
            this.max = 0;
            updateGauge();
        }

        public void setValue(long value) {
            this.value = value;
            updateGauge();
        }

        public void setMinMax(long min, long max) {
            this.min = min ;
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
                float percent = 1f * (value - min) / (max - min);
                // Show percent of level.
                gauge.setValue(percent, String.format("%.2f%%", percent * 100));
            } else {
                float percent = 1f - (1f * max / value);
                // Show percent xp buffer over next level (?!)
                gauge.setValue(percent, String.format("%.2f%% (buffer)", percent * 100));
            }
        }

        public String toString() {
            return "" + value + "/(" + min + ':' + max + ')';
        }
    }

    @Data
    public static class Party {
        String name; /* TODO, not implemented in protocol */
        Map<String, PartyMemberStatus> members = new HashMap<>();
    }

    @Data
    public static class PartyMemberStatus {
        PartyMember member;
        float hp = 1f;
        float mp = 1f;
        float sp = 1f;

        public String toString() {
            return "Class: " + member.getCharacterClass().stream().map(CharacterClass::getDisplayName)
                    .collect(joining("/")) +
                    " Gender: " + member.getGender().getDisplayName() +
                    " Race: " + member.getRace().getDisplayName() +
                    " Level: " + member.getLevel() +
                    String.format(" Hp: %.2f%% Mp: %.2f%% Sp: %.2f%%", hp * 100, mp * 100, sp * 100);
        }
    }

    public String toString() {
        return "Name: " +
                Optional.ofNullable(name).orElse("Not set") +
                '\n' +
                "Fullname: " +
                Optional.ofNullable(fullname).orElse("Not set") +
                '\n' +
                "Gender: " +
                Optional.ofNullable(gender).map(Gender::getDisplayName).orElse("Not set") +
                '\n' +
                "Race: " +
                Optional.ofNullable(race).map(Race::getDisplayName).orElse("Not set") +
                '\n' +
                "Class: " +
                getCharacterClass().stream().map(CharacterClass::getDisplayName).collect(joining("/")) +
                '\n' +
                "Level: " +
                Optional.ofNullable(getLevel()).map(Object::toString).orElse("Not set") +
                '\n' +
                "Hp: " +
                hp.toString() +
                " Mp: " +
                mp.toString() +
                " Sp: " +
                sp.toString() +
                " Xp: " +
                xp.toString() +
                "\nStats:\n    " +
                stats.entrySet().stream()
                        .map(entry -> entry.getKey().getDisplayName() + ": " + entry.getValue())
                        .collect(joining("\n    ")) +
                "\nSkills:\n    " +
                skills.entrySet().stream()
                        .map(entry -> entry.getKey().getDisplayName() + ": " + entry.getValue())
                        .collect(joining("\n    ")) +
                "\nLimbs:\n    " +
                limbs.entrySet().stream()
                        .map(entry -> entry.getKey().getDisplayName() + ": " + entry.getValue())
                        .collect(joining("\n    ")) +
                "\nParty:\n    " +
                party.getMembers().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .collect(joining("\n    "));
    }
}
