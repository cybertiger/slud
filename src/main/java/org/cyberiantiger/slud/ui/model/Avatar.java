package org.cyberiantiger.slud.ui.model;

import lombok.Getter;
import org.cyberiantiger.slud.model.*;
import org.cyberiantiger.slud.model.GmcpHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public final class Avatar extends AbstractChangable<Void, Avatar> implements GmcpHandler {
    // Child objects.
    @Getter
    private final AvatarVital hp;
    @Getter
    private final AvatarVital mp;
    @Getter
    private final AvatarVital sp;
    @Getter
    private final AvatarExperience xp;
    @Getter
    private AvatarClass avatarClass;
    @Getter
    private final AvatarStats stats;
    @Getter
    private final AvatarSkills skills;
    @Getter
    private final AvatarLimbs limbs;
    @Getter
    private final AvatarParty party;
    // Properties of this object.
    @Getter
    private final List<Changeable<Avatar, ?>> children;
    @Getter
    private String name;
    @Getter
    private String fullname;
    @Getter
    private Gender gender;
    @Getter
    private Race race;
    @Getter
    private Integer level;

    @Inject
    public Avatar(AvatarVital hp,
                  AvatarVital mp,
                  AvatarVital sp,
                  AvatarExperience xp,
                  AvatarClass avatarClass,
                  AvatarStats stats,
                  AvatarSkills skills,
                  AvatarLimbs limbs,
                  AvatarParty party) {
        super(null);
        this.hp = hp;
        this.mp = mp;
        this.sp = sp;
        this.xp = xp;
        this.avatarClass = avatarClass;
        this.stats = stats;
        this.skills = skills;
        this.limbs = limbs;
        this.party = party;
        children = Arrays.asList(hp, mp, sp, xp, avatarClass, stats, skills, limbs, party);
        reset();
    }

    public void reset() {
        super.reset();
        name = null;
        fullname = null;
        gender = null;
        race = null;
        level = null;
    }

    @Override
    public void handleGmcpCharReset(int arg) {
        reset();
    }

    @Override
    public void handleGmcpCharQuit(int arg) {
        reset();
    }

    @Override
    public void handleGmcpCharVitalsHp(int hp) {
        this.hp.setValue(hp);
    }

    @Override
    public void handleGmcpCharVitalsMaxHp(int maxhp) {
        this.hp.setMax(maxhp);
    }

    @Override
    public void handleGmcpCharVitalsMp(int mp) {
        this.mp.setValue(mp);
    }

    @Override
    public void handleGmcpCharVitalsMaxMp(int maxmp) {
        this.mp.setMax(maxmp);
    }

    @Override
    public void handleGmcpCharVitalsSp(int sp) {
        this.sp.setValue(sp);
    }

    @Override
    public void handleGmcpCharVitalsMaxSp(int maxsp) {
        this.sp.setMax(maxsp);
    }

    @Override
    public void handleGmcpCharVitalsXp(long xp) {
        this.xp.setValue(xp);
    }

    @Override
    public void handleGmcpCharVitalsMinXp(long minxp) {
        this.xp.setMin(minxp);
    }

    @Override
    public void handleGmcpCharVitalsMaxXp(long maxxp) {
        this.xp.setMax(maxxp);
    }

    @Override
    public void handleGmcpCharStatus(CharStatus status) {
        this.name = status.getName();
        this.fullname = status.getFullname();
        this.gender = status.getGender();
        this.race = status.getRace();
        this.level = status.getLevel();
        setChanged();
        // TODO: Guild.
    }

    @Override
    public void handleGmcpCharStats(EnumMap<Stat, Integer> stats) {
    }

    @Override
    public void handleGmcpCharSkills(EnumMap<Skill, Integer> skills) {
    }

    @Override
    public void handleGmcpCharLimbs(EnumMap<Limb, LimbStatus> limbs) {
    }

    @Override
    public void handleGmcpCharItems(Map<String, Item> items) {
    }

    @Override
    public void handleGmcpCharItemsBag(Map<String, Map<String, Item>> bagItems) {
    }

    @Override
    public void handleGmcpCharWorn(Map<String, EnumSet<Limb>> worn) {
    }

    @Override
    public void handleGmcpCharWielded(Map<String, EnumSet<Limb>> worn) {
    }

    @Override
    public void handleGmcpCharHunt(List<String> hunters) {
    }

    @Override
    public void handleGmcpCharAttackersAttack(List<String> attackers) {
    }

    @Override
    public void handleGmcpCharTargetVitals(TargetVitals vitals) {
    }

    @Override
    public void handleGmcpRoomItems(Map<String, Item> items) {
    }

    @Override
    public void handleGmcpRoomItemsAll(List<Map<String, Item>> items) {
    }

    @Override
    public void handleGmcpPartyMembers(Map<String, PartyMember> party) {
        // delegate to party.
        this.party.handleGmcpPartyMembers(party);
    }

    @Override
    public void handleGmcpPartyVitals(Map<String, PartyVitals> party) {
        // delegate to party.
        this.party.handleGmcpPartyVitals(party);
    }
}
