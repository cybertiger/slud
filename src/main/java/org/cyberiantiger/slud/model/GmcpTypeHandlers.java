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
        handlerMap.put("Char.Vitals.maxexp", new GmcpTypeHandler<Long>() {
            @Override
            public Consumer<Ui> getHandler(Long data) {
                return ui -> ui.gmcpMaxXp(data);
            }
        });
        handlerMap.put("Char.Stats", new GmcpTypeHandler<EnumMap<Stat, Integer>>() {
            @Override
            public Consumer<Ui> getHandler(EnumMap<Stat, Integer> data) {
                log.info("Char.Stats: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Skills", new GmcpTypeHandler<EnumMap<Skill, Integer>>() {
            @Override
            public Consumer<Ui> getHandler(EnumMap<Skill, Integer> data) {
                log.info("Char.Skills: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Status", new GmcpTypeHandler<CharStatus>() {
            @Override
            public Consumer<Ui> getHandler(CharStatus data) {
                log.info("Char.Status: {}", data);
                return ui -> {};
            }
        });
        handlerMap.put("Char.Limbs", new GmcpTypeHandler<EnumMap<Limb, LimbStatus>>() {
            public Consumer<Ui> getHandler(EnumMap<Limb, LimbStatus> data) {
                log.info("Char.Limbs: {}", data);
                return ui -> {};
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
        this.handlerMap = handlerMap;
    }

    public GmcpTypeHandler<?> getGmcpTypeHandler(String gmcpType) {
        return handlerMap.get(gmcpType);
    }
}
