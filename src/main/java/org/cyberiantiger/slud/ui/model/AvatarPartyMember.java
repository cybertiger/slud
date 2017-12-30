package org.cyberiantiger.slud.ui.model;

import dagger.Lazy;
import lombok.Getter;
import org.cyberiantiger.slud.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class AvatarPartyMember extends AbstractChangable<AvatarParty, AvatarPartyMember> {
    @Getter
    private final String name;
    @Getter
    private int level;
    @Getter
    private Gender gender;
    @Getter
    private Race race;
    @Getter
    private CharacterClass primaryClass;
    @Getter
    private CharacterClass secondaryClass;
    @Getter
    private float hp = 0;
    @Getter
    private float mp = 0;
    @Getter
    private float sp = 0;


    protected AvatarPartyMember(Lazy<AvatarParty> parent, String name) {
        super(parent);
        this.name = name;
    }

    @Override
    public Collection<Changeable<AvatarPartyMember, ?>> getChildren() {
        return emptyList();
    }

    public void updateDetails(PartyMember update) {
        this.level = update.getLevel();
        this.race = update.getRace();
        this.gender = update.getGender();
        List<CharacterClass> characterClass = update.getCharacterClass();
        this.primaryClass = characterClass.get(0);
        if (characterClass.size() > 1) {
            secondaryClass = characterClass.get(1);
        } else {
            secondaryClass = null;
        }
        setChanged();
    }

    public void updateVitals(PartyVitals v) {
        Optional.ofNullable(v.getHp()).ifPresent(hp -> this.hp = hp);
        Optional.ofNullable(v.getMp()).ifPresent(mp -> this.mp = mp);
        Optional.ofNullable(v.getSp()).ifPresent(sp -> this.sp = sp);
        setChanged();
    }
}
