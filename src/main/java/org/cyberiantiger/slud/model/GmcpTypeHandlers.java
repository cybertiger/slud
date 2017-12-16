package org.cyberiantiger.slud.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum GmcpTypeHandlers {
    INSTANCE;
    private static final Logger log = LoggerFactory.getLogger(GmcpTypeHandlers.class);

    private Map<String, GmcpTypeHandler<?>> handlerMap;

    GmcpTypeHandlers() {
        Map<String, GmcpTypeHandler<?>> handlerMap = new HashMap<>();
        handlerMap.put("Char.Quit", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Quit: {}", data);
            }
        });
        handlerMap.put("Char.Reset", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Reset: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.hp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.hp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.mp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.mp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.sp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.sp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.exp", new GmcpTypeHandler<Long>() {
            @Override
            public void handle(Long data) {
                log.info("Char.Vitals.exp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.maxhp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.maxhp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.maxmp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.maxmp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.maxsp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.maxsp: {}", data);
            }
        });
        handlerMap.put("Char.Vitals.maxexp", new GmcpTypeHandler<Integer>() {
            @Override
            public void handle(Integer data) {
                log.info("Char.Vitals.maxexp: {}", data);
            }
        });
        handlerMap.put("Char.Stats", new GmcpTypeHandler<Map<Stat, Integer>>() {
            @Override
            public void handle(Map<Stat, Integer> data) {
                log.info("Char.Stats: {}", data);
            }
        });
        handlerMap.put("Char.Skills", new GmcpTypeHandler<Map<Skill, Integer>>() {
            @Override
            public void handle(Map<Skill, Integer> data) {
                log.info("Char.Skills: {}", data);
            }
        });
        handlerMap.put("Char.Status", new GmcpTypeHandler<CharStatus>() {
            @Override
            public void handle(CharStatus data) {
                log.info("Char.Status: {}", data);
            }
        });
        handlerMap.put("Char.Limbs", new GmcpTypeHandler<Map<Limb, LimbStatus>>() {
            @Override
            public void handle(Map<Limb, LimbStatus> data) {
                log.info("Char.Limbs {}", data);
            }
        });
        this.handlerMap = Collections.unmodifiableMap(handlerMap);
    }

    public GmcpTypeHandler<?> getGmcpTypeHandler(String gmcpType) {
        return handlerMap.get(gmcpType);
    }
}
