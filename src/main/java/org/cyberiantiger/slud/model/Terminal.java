package org.cyberiantiger.slud.model;

import com.googlecode.lanterna.TextColor;

public interface Terminal {
    void clear();

    void newLine();

    void carriageReturn();

    void write(String data);

    void reset();

    void setForeground(TextColor color);

    void setBackground(TextColor color);

    void setBold(boolean bold);

    void setFlash(boolean flash);

    void setItalic(boolean italic);

    void setUnderline(boolean underline);

    void reverse();

    void resetAttributes();

    void beep();

    void flush();

    void setLocalEcho(boolean echo);

    void localEcho(String text);
}
