package org.cyberiantiger.slud.net;

import org.cyberiantiger.slud.ui.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private List<Consumer<Console>> actions = new ArrayList<>();
    private AnsiState state = NORMAL;

    public enum AnsiState {
        NORMAL,
        AFTER_ESCAPE,
        AFTER_LEFT_ANGLE;
    }

    public void handleRead(CharBuffer data) {
        while(data.hasRemaining()) {
            handleRead(data.get());
        }
        flushActions();
    }

    private static final Console debugConsole = new Console() {
        @Override
        public void clear() {
            log.info("Console.clear()");
        }

        @Override
        public void setX(int x) {
            log.info("Console.setX({})", x);
        }

        @Override
        public void moveX(int x) {
            log.info("Console.moveX({})", x);
        }

        @Override
        public void setY(int y) {
            log.info("Console.setY({})", y);
        }

        @Override
        public void moveY(int y) {
            log.info("Console.moveY({})", y);
        }

        @Override
        public void write(String data) {
            log.info("Console.write({})", data);
        }

        @Override
        public void reset() {
            log.info("Console.reset()");
        }

        @Override
        public void setForeground(ConsoleColor color) {
            log.info("Console.setForeground({})", color);
        }

        @Override
        public void setBackground(ConsoleColor color) {
            log.info("Console.setBackground({})", color);
        }

        @Override
        public void setBold(boolean bold) {
            log.info("Console.setBold({})", bold);
        }

        @Override
        public void setFlash(boolean flash) {
            log.info("Console.setFlash({})", flash);
        }

        @Override
        public void setItalic(boolean italic) {
            log.info("Console.setItalic({})", italic);
        }

        @Override
        public void setUnderline(boolean underline) {
            log.info("Console.setUnderline({})", underline);
        }

        @Override
        public void reverse() {
            log.info("Console.reverse()");
        }

        @Override
        public void resetAttributes() {
            log.info("Console.resetAttributes()");
        }

        @Override
        public void beep() {
            log.info("Console.beep()");
        }
    };

    private void flushActions() {
        for (Consumer<Console> action : actions) {
            action.accept(debugConsole);
        }
        actions.clear();
    }

    private void handleRead(char ch) {
        switch (state) {
            case NORMAL:
                switch (ch) {
                    case '\r': // carriage return
                        flushCharacters();
                        actions.add((console) -> console.setX(0));
                        break;
                    case '\n': // new line
                        flushCharacters();
                        actions.add((console) -> console.moveY(1));
                        break;
                    case '\007': // bell
                        actions.add(Console::beep);
                        break;
                    case '\014': // clear screen
                        flushCharacters();
                        actions.add(Console::clear);
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
                    case 'c': // Console reset.
                        actions.add(Console::reset);
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
                            actions.add(Console::clear);
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
                    actions.add(Console::resetAttributes);
                    break;
                case 1:
                    actions.add(console -> console.setBold(true));
                    break;
                case 3:
                    actions.add(console -> console.setItalic(true));
                    break;
                case 4:
                    actions.add(console -> console.setUnderline(true));
                    break;
                case 5:
                    actions.add(console -> console.setFlash(true));
                    break;
                case 7:
                    actions.add(Console::reverse);
                    break;
                case 22:
                    actions.add(console -> console.setBold(false));
                    break;
                case 23:
                    actions.add(console -> console.setItalic(false));
                    break;
                case 24:
                    actions.add(console -> console.setUnderline(false));
                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    Console.ConsoleColor fgColor = Console.ConsoleColor.values()[i - 30];
                    actions.add(console -> console.setForeground(fgColor));
                    break;
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                    Console.ConsoleColor bgColor = Console.ConsoleColor.values()[i - 40];
                    actions.add(console -> console.setBackground(bgColor));
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
        actions.add(console -> console.write(data));
    }
}
