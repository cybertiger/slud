package org.cyberiantiger.slud.net;

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
    private StringBuilder charOut = new StringBuilder();
    private StringBuilder sgrNumber = new StringBuilder();
    private List<Integer> sgrCommands = new ArrayList<>();
    private List<Consumer<Ui>> actions = new ArrayList<>();
    private AnsiState state = NORMAL;
    private Slud main;

    public enum AnsiState {
        NORMAL,
        AFTER_ESCAPE,
        AFTER_LEFT_ANGLE;
    }

    @Inject
    public AnsiCodec(Slud main) {
        this.main = main;
    }

    public void handleRead(CharBuffer data) {
        while(data.hasRemaining()) {
            handleRead(data.get());
        }
        flushCharacters();
        flushActions();
    }

    private void flushActions() {
        // Pass actions to UI thread.
        main.runInUi(actions);
        actions.clear();
    }

    private void handleRead(char ch) {
        switch (state) {
            case NORMAL:
                switch (ch) {
                    case '\r': // carriage return
                        flushCharacters();
                        actions.add((ui) -> ui.setX(0));
                        break;
                    case '\n': // new line
                        flushCharacters();
                        actions.add((ui) -> ui.moveY(1));
                        break;
                    case '\007': // bell
                        actions.add(Ui::beep);
                        break;
                    case '\014': // clear screen
                        flushCharacters();
                        actions.add(Ui::clear);
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
                        actions.add(Ui::reset);
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
                            actions.add(Ui::clear);
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
                    actions.add(Ui::resetAttributes);
                    break;
                case 1:
                    actions.add(ui -> ui.setBold(true));
                    break;
                case 3:
                    actions.add(ui -> ui.setItalic(true));
                    break;
                case 4:
                    actions.add(ui -> ui.setUnderline(true));
                    break;
                case 5:
                    actions.add(ui -> ui.setFlash(true));
                    break;
                case 7:
                    actions.add(Ui::reverse);
                    break;
                case 22:
                    actions.add(ui -> ui.setBold(false));
                    break;
                case 23:
                    actions.add(ui -> ui.setItalic(false));
                    break;
                case 24:
                    actions.add(ui -> ui.setUnderline(false));
                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    Ui.ConsoleColor fgColor = Ui.ConsoleColor.values()[i - 30];
                    actions.add(ui -> ui.setForeground(fgColor));
                    break;
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                    Ui.ConsoleColor bgColor = Ui.ConsoleColor.values()[i - 40];
                    actions.add(ui -> ui.setBackground(bgColor));
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
        actions.add(ui -> ui.write(data));
    }
}
