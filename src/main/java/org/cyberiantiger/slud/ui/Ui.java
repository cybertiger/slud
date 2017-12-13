package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.TextColor;

public interface Ui {
    enum ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED;
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
