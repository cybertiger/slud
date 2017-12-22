package org.cyberiantiger.slud.model;

import org.cyberiantiger.slud.ui.Ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public enum GmcpTypeHandlers {
    INSTANCE;
    private static final Logger log = LoggerFactory.getLogger(GmcpTypeHandlers.class);

    private Map<String, GmcpTypeHandler<?>> handlerMap;

    GmcpTypeHandlers() {
        Map<String, GmcpTypeHandler<?>> handlerMap = new HashMap<>();
        handlerMap.put("Char.Quit", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return Ui::gmcpQuit;
            }
        });
        handlerMap.put("Char.Reset", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return Ui::gmcpReset;
            }
        });
        handlerMap.put("Char.Vitals.hp", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return ui -> ui.gmcpHp(data);
            }
        });
        handlerMap.put("Char.Vitals.mp", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return ui -> ui.gmcpMp(data);
            }
        });
        handlerMap.put("Char.Vitals.sp", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return ui -> ui.gmcpSp(data);
            }
        });
        handlerMap.put("Char.Vitals.exp", new GmcpTypeHandler<Long>() {
            @Override
            public Consumer<Ui> getHandler(Long data) {
                return ui -> ui.gmcpXp(data);
            }
        });
        handlerMap.put("Char.Vitals.maxhp", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return ui -> ui.gmcpMaxHp(data);
            }
        });
        handlerMap.put("Char.Vitals.maxmp", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return ui -> ui.gmcpMaxMp(data);
            }
        });
        handlerMap.put("Char.Vitals.maxsp", new GmcpTypeHandler<Integer>() {
            @Override
            public Consumer<Ui> getHandler(Integer data) {
                return ui -> ui.gmcpMaxSp(data);
            }
        });
        handlerMap.put("Char.Vitals.maxexp", new GmcpTypeHandler<MaxXp>() {
            @Override
            public Consumer<Ui> getHandler(MaxXp data) {
                return ui -> ui.gmcpMaxXp(data.getMin(), data.getMax());
            }
        });
        handlerMap.put("Char.Stats", new GmcpTypeHandler<EnumMap<Stat, Integer>>() {
            @Override
            public Consumer<Ui> getHandler(EnumMap<Stat, Integer> data) {
                return ui -> ui.gmcpCharStats(data);
            }
        });
        handlerMap.put("Char.Skills", new GmcpTypeHandler<EnumMap<Skill, Integer>>() {
            @Override
            public Consumer<Ui> getHandler(EnumMap<Skill, Integer> data) {
                return ui -> ui.gmcpCharSkills(data);
            }
        });
        handlerMap.put("Char.Status", new GmcpTypeHandler<CharStatus>() {
            @Override
            public Consumer<Ui> getHandler(CharStatus data) {
                return ui -> ui.gmcpCharStatus(data);
            }
        });
        handlerMap.put("Char.Limbs", new GmcpTypeHandler<EnumMap<Limb, LimbStatus>>() {
            public Consumer<Ui> getHandler(EnumMap<Limb, LimbStatus> data) {
                return ui -> ui.gmcpCharLimbs(data);
            }
        });
        handlerMap.put("Char.Items", new GmcpTypeHandler<Map<String, Item>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, Item> data) {
                log.info("Char.Items: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Items.Bag", new GmcpTypeHandler<Map<String, Map<String, Item>>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, Map<String, Item>> data) {
                log.info("Char.Items: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Room.Items", new GmcpTypeHandler<Map<String, Item>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, Item> data) {
                log.info("Room.Items: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Room.Items.All", new GmcpTypeHandler<List<Map<String, Item>>>() {
            @Override
            public Consumer<Ui> getHandler(List<Map<String, Item>> data) {
                log.info("Room.Items.All: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Worn", new GmcpTypeHandler<Map<String, EnumSet<Limb>>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, EnumSet<Limb>> data) {
                log.info("Char.Worn: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Wielded", new GmcpTypeHandler<Map<String, EnumSet<Limb>>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, EnumSet<Limb>> data) {
                log.info("Char.Wielded: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Hunt", new GmcpTypeHandler<List<String>>() {
            @Override
            public Consumer<Ui> getHandler(List<String> data) {
                log.info("Char.Attackers.Hunt: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Attackers.Attack", new GmcpTypeHandler<List<String>>() {
            @Override
            public Consumer<Ui> getHandler(List<String> data) {
                log.info("Char.Attackers.Attack: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Target.Vitals", new GmcpTypeHandler<TargetVitals>() {
            @Override
            public Consumer<Ui> getHandler(TargetVitals data) {
                log.info("Char.Target.Vitals: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Party.Members", new GmcpTypeHandler<Map<String, PartyMember>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, PartyMember> data) {
                return ui -> ui.gmcpPartyMembers(data);
            }
        });
        handlerMap.put("Party.Vitals", new GmcpTypeHandler<Map<String, PartyVitals>>() {
            @Override
            public Consumer<Ui> getHandler(Map<String, PartyVitals> data) {
                return ui -> ui.gmcpPartyVitals(data);
            }
        });
        // TODO:
        // Char.Target (not yet implemented, server side)
        // Char.Cast (would like to make a list of all castable spells).
        // Char.Buffs (would like to a make a list of all possible buffs).

        this.handlerMap = handlerMap;
    }

    public GmcpTypeHandler<?> getGmcpTypeHandler(String gmcpType) {
        return handlerMap.get(gmcpType);
    }
}
