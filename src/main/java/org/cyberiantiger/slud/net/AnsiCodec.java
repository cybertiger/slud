package org.cyberiantiger.slud.net;

import com.googlecode.lanterna.TextColor;
import dagger.Lazy;
import org.cyberiantiger.slud.Slud;
import org.cyberiantiger.slud.ui.Ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.cyberiantiger.slud.net.AnsiCodec.AnsiState.*;

public class AnsiCodec {
    private static final Logger log = LoggerFactory.getLogger(AnsiCodec.class);
    private final StringBuilder charOut = new StringBuilder();
    private final StringBuilder sgrNumber = new StringBuilder();
    private final List<Integer> sgrCommands = new ArrayList<>();
    private final Slud main;
    private final Lazy<TelnetSocketChannelHandler> handler;
    private AnsiState state = NORMAL;

    public enum AnsiState {
        NORMAL,
        AFTER_ESCAPE,
        AFTER_LEFT_ANGLE;
    }

    @Inject
    public AnsiCodec(Slud main, Lazy<TelnetSocketChannelHandler> handler) {
        this.main = main;
        this.handler = handler;
    }

    private void addAction(Consumer<Ui> action) {
        handler.get().addUiAction(action);
    }

    public void handleRead(CharBuffer data) {
        while(data.hasRemaining()) {
            handleRead(data.get());
        }
        flushCharacters();
    }

    private void handleRead(char ch) {
        switch (state) {
            case NORMAL:
                switch (ch) {
                    case '\r': // carriage return
                        flushCharacters();
                        addAction(Ui::carriageReturn);
                        break;
                    case '\n': // new line
                        flushCharacters();
                        addAction(Ui::newLine);
                        break;
                    case '\007': // bell
                        addAction(Ui::beep);
                        break;
                    case '\014': // clear screen
                        flushCharacters();
                        addAction(Ui::clear);
                        break;
                    case '\033': // ANSI escape
                        flushCharacters();
                        state = AFTER_ESCAPE;
                        break;
                    default:
                        charOut.append(ch);
                }
                break;
            case AFTER_ESCAPE:
                switch (ch) {
                    case '[': // Start of ANSI escape.
                        state = AFTER_LEFT_ANGLE;
                        break;
                    case 'c': // Ui reset.
                        addAction(Ui::reset);
                        state = NORMAL;
                        break;
                    default:
                        log.warn("Unhandled ANSI escape {}", ch);
                        state = NORMAL;
                        break;
                }
                break;
            case AFTER_LEFT_ANGLE:
                if (ch >= '0' && ch <= '9') {
                    sgrNumber.append(ch);
                } else {
                    flushSgr();
                    switch (ch) {
                        case ';':
                            /* NOOP */
                            break;
                        case 'm':
                            handleSgr();
                            state = NORMAL;
                            break;
                        case 'J':
                            // TODO: sgrCommands should hold a number which modifies the clear screen action.
                            addAction(Ui::clear);
                            sgrCommands.clear();
                            state = NORMAL;
                            break;
                        default:
                            // TODO: Loads of horrible codes.
                            log.warn("Unhandled ANSI escape: {} with args {}", ch, sgrCommands);
                            sgrCommands.clear();
                            state = NORMAL;
                            /* unhandled ANSI escapes */
                            break;
                    }
                }
                break;
        }
    }

    private void handleSgr() {
        for (int i : sgrCommands) {
            switch (i) {
                case 0:
                    addAction(Ui::resetAttributes);
                    break;
                case 1:
                    addAction(ui -> ui.setBold(true));
                    break;
                case 3:
                    addAction(ui -> ui.setItalic(true));
                    break;
                case 4:
                    addAction(ui -> ui.setUnderline(true));
                    break;
                case 5:
                    addAction(ui -> ui.setFlash(true));
                    break;
                case 7:
                    addAction(Ui::reverse);
                    break;
                case 22:
                    addAction(ui -> ui.setBold(false));
                    break;
                case 23:
                    addAction(ui -> ui.setItalic(false));
                    break;
                case 24:
                    addAction(ui -> ui.setUnderline(false));
                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    TextColor fgColor = TextColor.ANSI.values()[i - 30];
                    addAction(ui -> ui.setForeground(fgColor));
                    break;
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                    TextColor bgColor = TextColor.ANSI.values()[i - 40];
                    addAction(ui -> ui.setBackground(bgColor));
                    break;
                default:
                    log.warn("Unhandled SGR in ANSI command: {}", i);
                    break;
            }
        }
        sgrCommands.clear();
    }

    private void flushSgr() {
        String sgr = sgrNumber.toString();
        try {
            sgrCommands.add(Integer.parseInt(sgr));
        } catch (NumberFormatException ex) {
            log.warn("Invalid number in SGR sequence: " + sgr);
        } finally {
            sgrNumber.setLength(0);
        }
    }

    private void flushCharacters() {
        if (charOut.length() == 0) {
            return;
        }
        String data = charOut.toString();
        charOut.setLength(0);
        addAction(ui -> ui.write(data));
    }
}
