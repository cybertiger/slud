package org.cyberiantiger.slud.ui.model;

import lombok.Data;
import org.cyberiantiger.slud.model.CharacterClass;
import org.cyberiantiger.slud.model.PartyMember;

import static java.util.stream.Collectors.joining;

@Data
public class PartyMemberStatus {
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
