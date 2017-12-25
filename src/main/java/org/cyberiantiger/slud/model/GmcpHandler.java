package org.cyberiantiger.slud.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GmcpHandler {
    @GmcpMessage("Char.Reset")
    void handleGmcpCharReset(int arg);

    @GmcpMessage("Char.Quit")
    void handleGmcpCharQuit(int arg);

    @GmcpMessage("Char.Vitals.hp")
    void handleGmcpCharVitalsHp(int hp);

    @GmcpMessage("Char.Vitals.maxhp")
    void handleGmcpCharVitalsMaxHp(int maxhp);

    @GmcpMessage("Char.Vitals.mp")
    void handleGmcpCharVitalsMp(int mp);

    @GmcpMessage("Char.Vitals.maxmp")
    void handleGmcpCharVitalsMaxMp(int maxmp);

    @GmcpMessage("Char.Vitals.sp")
    void handleGmcpCharVitalsSp(int sp);

    @GmcpMessage("Char.Vitals.maxsp")
    void handleGmcpCharVitalsMaxSp(int maxsp);

    @GmcpMessage("Char.Vitals.exp")
    void handleGmcpCharVitalsXp(long xp);

    @GmcpMessage("Char.Vitals.minexp")
    void handleGmcpCharVitalsMinXp(long minxp);

    @GmcpMessage("Char.Vitals.maxexp")
    void handleGmcpCharVitalsMaxXp(long maxxp);

    @GmcpMessage("Char.Status")
    void handleGmcpCharStatus(CharStatus status);

    @GmcpMessage("Char.Stats")
    void handleGmcpCharStats(Map<Stat, Integer> stats);

    @GmcpMessage("Char.Skills")
    void handleGmcpCharSkills(Map<Skill, Integer> skills);

    @GmcpMessage("Char.Limbs")
    void handleGmcpCharLimbs(Map<Limb, LimbStatus> limbs);

    @GmcpMessage("Char.Items")
    void handleGmcpCharItems(Map<String, Item> items);

    @GmcpMessage("Char.Items.Bag")
    void handleGmcpCharItemsBag(Map<String, Map<String, Item>> bagItems);

    @GmcpMessage("Char.Worn")
    void handleGmcpCharWorn(Map<String, Set<Limb>> worn);

    @GmcpMessage("Char.Wielded")
    void handleGmcpCharWielded(Map<String, Set<Limb>> worn);

    @GmcpMessage("Char.Hunt")
    void handleGmcpCharHunt(List<String> hunters);

    @GmcpMessage("Char.Attackers.Attack")
    void handleGmcpCharAttackersAttack(List<String> attackers);

    @GmcpMessage("Char.Target.Vitals")
    void handleGmcpCharTargetVitals(TargetVitals vitals);

    @GmcpMessage("Room.Items")
    void handleGmcpRoomItems(Map<String, Item> items);

    @GmcpMessage("Room.Items.All")
    void handleGmcpRoomItemsAll(List<Map<String, Item>> items);
}