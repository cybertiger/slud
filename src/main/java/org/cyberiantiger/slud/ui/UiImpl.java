package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import org.cyberiantiger.slud.model.*;
import org.cyberiantiger.slud.ui.model.Avatar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Gather Ui components, and in the darkness bind them.
 *
 * TODO: Abuse a proxy to delegate calls to the correct handler implementation.
 */
@Singleton
public class UiImpl implements Ui {
    private static final Logger log = LoggerFactory.getLogger(UiImpl.class);
    private final SludUi ui;
    private final Avatar avatar;
    private final ScrollingSwingTerminal output;
    private TextColor fg;
    private TextColor bg;
    private boolean echo = true;

    @Inject
    public UiImpl(SludUi ui, Avatar avatar) {
        this.ui = ui;
        this.avatar = avatar;
        this.output = ui.getOutputField();
        resetColorsInternal();
    }

    private void resetColorsInternal() {
        fg = TextColor.ANSI.WHITE;
        bg = TextColor.ANSI.BLACK;
    }

    @Override
    public void clear() {
        ui.getOutputField().clearScreen();
    }

    @Override
    public void newLine() {
        output.putCharacter('\n');
    }

    @Override
    public void carriageReturn() {
        output.putCharacter('\r');
    }

    @Override
    public void write(String data) {
        for (int i = 0; i < data.length(); i++) {
            output.putCharacter(data.charAt(i));
        }
    }

    @Override
    public void reset() {
        output.clearScreen();
        output.resetColorAndSGR();
        resetColorsInternal();
    }

    @Override
    public void setForeground(TextColor color) {
        output.setForegroundColor(color);
        fg = color;
    }

    @Override
    public void setBackground(TextColor color) {
        output.setBackgroundColor(color);
        bg = color;
    }

    @Override
    public void setBold(boolean bold) {
        if (bold) {
            output.enableSGR(SGR.BOLD);
        } else {
            output.disableSGR(SGR.BOLD);
        }
    }

    @Override
    public void setFlash(boolean flash) {
        if (flash) {
            output.enableSGR(SGR.BLINK);
        } else {
            output.disableSGR(SGR.BLINK);
        }
    }

    @Override
    public void setItalic(boolean italic) {
        if (italic) {
            output.enableSGR(SGR.ITALIC);
        } else {
            output.disableSGR(SGR.ITALIC);
        }
    }

    @Override
    public void setUnderline(boolean underline) {
        if (underline) {
            output.enableSGR(SGR.UNDERLINE);
        } else {
            output.disableSGR(SGR.UNDERLINE);
        }
    }

    @Override
    public void reverse() {
        TextColor bg = this.fg;
        this.fg = this.bg;
        this.bg = bg;
        output.setForegroundColor(fg);
        output.setBackgroundColor(bg);
    }

    @Override
    public void resetAttributes() {
        resetColorsInternal();
        output.resetColorAndSGR();
    }

    @Override
    public void beep() {
        output.bell();
    }

    @Override
    public void flush() {
        output.flush();
        avatar.flushChanges();
    }

    @Override
    public void setLocalEcho(boolean echo) {
        this.echo = echo;
    }

    @Override
    public void localEcho(String text) {
        if (echo) {
            for (int i = 0; i < text.length(); i++) {
                output.putCharacter(text.charAt(i));
            }
            output.putCharacter('\r');
            output.putCharacter('\n');
            output.flush();
        }
        DefaultBoundedRangeModel test;
    }

    @Override
    public void setConnectionStatus(ConnectionStatus status) {
        ui.setConnectionStatus(status);
    }

    @Override
    public void handleGmcpCharReset(int arg) {
        avatar.handleGmcpCharReset(arg);
    }

    @Override
    public void handleGmcpCharQuit(int arg) {
        avatar.handleGmcpCharQuit(arg);
    }

    @Override
    public void handleGmcpCharVitalsHp(int hp) {
        avatar.handleGmcpCharVitalsHp(hp);
    }

    @Override
    public void handleGmcpCharVitalsMaxHp(int maxhp) {
        avatar.handleGmcpCharVitalsMaxHp(maxhp);
    }

    @Override
    public void handleGmcpCharVitalsMp(int mp) {
        avatar.handleGmcpCharVitalsMp(mp);
    }

    @Override
    public void handleGmcpCharVitalsMaxMp(int maxmp) {
        avatar.handleGmcpCharVitalsMaxMp(maxmp);
    }

    @Override
    public void handleGmcpCharVitalsSp(int sp) {
        avatar.handleGmcpCharVitalsSp(sp);
    }

    @Override
    public void handleGmcpCharVitalsMaxSp(int maxsp) {
        avatar.handleGmcpCharVitalsMaxSp(maxsp);
    }

    @Override
    public void handleGmcpCharVitalsXp(long xp) {
        avatar.handleGmcpCharVitalsXp(xp);
    }

    @Override
    public void handleGmcpCharVitalsMinXp(long minxp) {
        avatar.handleGmcpCharVitalsMinXp(minxp);
    }

    @Override
    public void handleGmcpCharVitalsMaxXp(long maxxp) {
        avatar.handleGmcpCharVitalsMaxXp(maxxp);
    }

    @Override
    public void handleGmcpCharStatus(CharStatus status) {
        avatar.handleGmcpCharStatus(status);
    }

    @Override
    public void handleGmcpCharStats(EnumMap<Stat, Integer> stats) {
        avatar.handleGmcpCharStats(stats);
    }

    @Override
    public void handleGmcpCharSkills(EnumMap<Skill, Integer> skills) {
        avatar.handleGmcpCharSkills(skills);
    }

    @Override
    public void handleGmcpCharLimbs(EnumMap<Limb, LimbStatus> limbs) {
        avatar.handleGmcpCharLimbs(limbs);
    }

    @Override
    public void handleGmcpCharItems(Map<String, Item> items) {
        avatar.handleGmcpCharItems(items);
    }

    @Override
    public void handleGmcpCharItemsBag(Map<String, Map<String, Item>> bagItems) {
        avatar.handleGmcpCharItemsBag(bagItems);
    }

    @Override
    public void handleGmcpCharWorn(Map<String, EnumSet<Limb>> worn) {
        avatar.handleGmcpCharWorn(worn);
    }

    @Override
    public void handleGmcpCharWielded(Map<String, EnumSet<Limb>> wielded) {
        avatar.handleGmcpCharWielded(wielded);
    }

    @Override
    public void handleGmcpCharHunt(List<String> hunters) {
        avatar.handleGmcpCharHunt(hunters);
    }

    @Override
    public void handleGmcpCharAttackersAttack(List<String> attackers) {
        avatar.handleGmcpCharAttackersAttack(attackers);
    }

    @Override
    public void handleGmcpCharTargetVitals(TargetVitals vitals) {
        avatar.handleGmcpCharTargetVitals(vitals);
    }

    @Override
    public void handleGmcpRoomItems(Map<String, Item> items) {
        avatar.handleGmcpRoomItems(items);
    }

    @Override
    public void handleGmcpRoomItemsAll(List<Map<String, Item>> items) {
        avatar.handleGmcpRoomItemsAll(items);
    }

    @Override
    public void handleGmcpPartyMembers(Map<String, PartyMember> party) {
        avatar.handleGmcpPartyMembers(party);
    }

    @Override
    public void handleGmcpPartyVitals(Map<String, PartyVitals> party) {
        avatar.handleGmcpPartyVitals(party);
    }

    @Override
    public void handleRoomId(Long roomId) {
        avatar.handleRoomId(roomId);
    }

    @Override
    public void handleRoomExits(Map<String, Long> exits) {
        avatar.handleRoomExits(exits);
    }

    @Override
    public void handleChat(ChatMessage chatMessage) {
        avatar.handleChat(chatMessage);
    }

    @Override
    public void handleTell(TellMessage tellMessage) {
        avatar.handleTell(tellMessage);
    }

    @Override
    public void handleCharMoved(String dir) {
        avatar.handleCharMoved(dir);
    }

    @Override
    public void handleRoomRealm(String realm) {
        avatar.handleRoomRealm(realm);
    }

    @Override
    public void handleCharCooldowns(Map<String, Integer> charCooldowns) {
        avatar.handleCharCooldowns(charCooldowns);
    }

    @Override
    public void handleCharBuffs(Map<String, CharBuff> buffMap) {
        avatar.handleCharBuffs(buffMap);
    }
}
