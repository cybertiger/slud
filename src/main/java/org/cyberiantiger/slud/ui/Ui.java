package org.cyberiantiger.slud.ui;

public interface Ui {
    enum ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED;
    }

    enum ConsoleColor {
        BLACK,
        RED,
        YELLOW,
        GREEN,
        CYAN,
        BLUE,
        MAGENTA,
        WHITE
    }

    /**
     * Clear visible area of screen, and move cursor to top left.
     */
    void clear();

    /**
     * Set x position of cursor relative to left hand size (0 based).
     * @param x
     */
    void setX(int x);

    /**
     * Move x position of cursor relative to current position.
     */
    void moveX(int x);

    /**
     * Set y position of cursor relative to top (0 based).
     * @param y
     */
    void setY(int y);

    /**
     * Move y position of cursor relative to current position.
     */
    void moveY(int y);

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
    void setForeground(ConsoleColor color);

    /**
     * Set background color.
     * @param color
     */
    void setBackground(ConsoleColor color);

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
     * Set connection status.
     */
    void setConnectionStatus(ConnectionStatus status);
}
