package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.TextColor;
import lombok.Getter;
import org.cyberiantiger.slud.net.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Ui {
    enum ConnectionStatus {
        DISCONNECTED(IconType.DISCONNECTED),
        CONNECTING(IconType.CONNECTING) {
            public void action(Network net) {
                net.disconnect();
            }
        },
        CONNECTED(IconType.CONNECTED) {
            public void action(Network net) {
                net.disconnect();
            }
        };
        private static final Logger log = LoggerFactory.getLogger(Ui.class);

        @Getter
        private final IconType iconType;

        ConnectionStatus(IconType iconType) {
            this.iconType = iconType;
        }

        public void action(Network net) {
            net.connect("elephant.org", 23, "ANSI");
        }
    }

    /**
     * Clear visible area of screen, and move cursor to top left.
     */
    void clear();

    /**
     * New line.
     */
    void newLine();

    /**
     * Carriage return.
     */
    void carriageReturn();

    /**
     * Write a string to the console.
     * @param data
     */
    void write(String data);

    /**
     * Reset console completely.
     */
    void reset();

    /**
     * Set foreground color.
     * @param color
     */
    void setForeground(TextColor color);

    /**
     * Set background color.
     * @param color
     */
    void setBackground(TextColor color);

    /**
     * Set bold text attribute.
     * @param bold
     */
    void setBold(boolean bold);

    /**
     * Set flash text attribute.
     * @param flash
     */
    void setFlash(boolean flash);

    /**
     * Set italic text attribute.
     * @param italic
     */
    void setItalic(boolean italic);

    /**
     * Set underline text attribute.
     */
    void setUnderline(boolean underline);

    /**
     * Swap foreground and background colors.
     */
    void reverse();

    /**
     * Reset text attributes to default.
     */
    void resetAttributes();

    /**
     * Beep.
     */
    void beep();

    /**
     * Flush output to screen.
     */
    void flush();

    /**
     * Toggle local echo.
     * @param echo Whether to echo locally or not.
     */
    void setLocalEcho(boolean echo);

    /**
     * Echo locally entered text.
     */
    void localEcho(String text);

    /**
     * Set connection status.
     */
    void setConnectionStatus(ConnectionStatus status);
}
