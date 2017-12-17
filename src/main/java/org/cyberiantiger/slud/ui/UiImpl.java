package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import org.cyberiantiger.slud.ui.model.Avatar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Gather Ui components, and in the darkness bind them.
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
        }
    }

    @Override
    public void setConnectionStatus(ConnectionStatus status) {
        ui.setConnectionStatus(status);
    }

    @Override
    public void gmcpQuit() {
        avatar.quit();
    }

    @Override
    public void gmcpReset() {
        avatar.reset();
    }

    @Override
    public void gmcpHp(int hp) {
        avatar.getHp().setValue(hp);
    }

    @Override
    public void gmcpMaxHp(int maxHp) {
        avatar.getHp().setMax(maxHp);
    }

    @Override
    public void gmcpMp(int mp) {
        avatar.getMp().setValue(mp);
    }

    @Override
    public void gmcpMaxMp(int maxMp) {
        avatar.getMp().setMax(maxMp);
    }

    @Override
    public void gmcpSp(int sp) {
        avatar.getSp().setValue(sp);
    }

    @Override
    public void gmcpMaxSp(int maxSp) {
        avatar.getSp().setMax(maxSp);
    }

    @Override
    public void gmcpXp(long xp) {
        avatar.getXp().setValue(xp);
    }

    @Override
    public void gmcpMaxXp(long maxXp) {
        avatar.getXp().setMax(maxXp);
    }
}
