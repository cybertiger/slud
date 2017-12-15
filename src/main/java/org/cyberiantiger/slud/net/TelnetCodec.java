package org.cyberiantiger.slud.net;

import dagger.Lazy;
import org.cyberiantiger.slud.net.option.OptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.cyberiantiger.slud.net.TelnetCodec.TelnetState.*;
import static org.cyberiantiger.slud.net.TelnetOption.*;

public class TelnetCodec {
    private static final Logger log = LoggerFactory.getLogger(TelnetCodec.class);
    private final Lazy<TelnetSocketChannelHandler> channelHandler;
    private final AnsiCodec ansiCodec;

    private byte sbType;
    private ByteBuffer sbOut;
    private ByteBuffer byteOut;
    private CharBuffer charOut;
    private CharsetDecoder charDecoder = StandardCharsets.UTF_8.newDecoder();
    private TelnetState state = NORMAL;
    private OptionHandler[] handlers = new OptionHandler[256];

    public enum TelnetState {
        NORMAL,
        AFTER_IAC,
        AFTER_DO,
        AFTER_DONT,
        AFTER_WILL,
        AFTER_WONT,
        AFTER_SB,
        AFTER_SB_TYPE,
        AFTER_SB_IAC;
    }

    @Inject
    public TelnetCodec(Lazy<TelnetSocketChannelHandler> channelHandler, AnsiCodec ansiCodec, Set<OptionHandler> handlers) {
        this.channelHandler = channelHandler;
        this.ansiCodec = ansiCodec;
        for (OptionHandler handler : handlers) {
            this.handlers[handler.getOption() & 0xff] = handler;
        }
        // Allocate some fixed size buffers....
        sbOut = ByteBuffer.allocate(0x100000);
        byteOut = ByteBuffer.allocate(0x100000);
        charOut = CharBuffer.allocate(0x100000);
    }

    @SuppressWarnings("unchecked")
    public <T extends OptionHandler> T getOptionHandler(byte type) {
        return (T) handlers[type & 0xff];
    }

    public void handleRead(ByteBuffer input) {
        while (input.hasRemaining()) {
            handleByte(input.get());
        }
        flushOutput();
    }

    public void handleConnect() {
        for (OptionHandler handler : handlers) {
            if (handler != null) {
                handler.handleConnect();
            }
        }
    }

    private void handleByte(byte data) {
        switch (state) {
            case NORMAL:
                if (data == IAC) {
                    state = AFTER_IAC;
                } else {
                    byteOut.put(data);
                }
                break;
            case AFTER_IAC:
                switch (data) {
                    case SB:
                        state = AFTER_SB;
                        break;
                    case WILL:
                        state = AFTER_WILL;
                        break;
                    case WONT:
                        state = AFTER_WONT;
                        break;
                    case DO:
                        state = AFTER_DO;
                        break;
                    case DONT:
                        state = AFTER_DONT;
                        break;
                    case IAC:
                        state = NORMAL;
                        byteOut.put(data);
                        break;
                    case NOP:
                    case DM:
                    case BRK:
                    case IP:
                    case AO:
                    case AYT:
                    case EC:
                    case EL:
                    case GA:
                    default:
                        // Includes handling non standard IAC <type> sequences.
                        handleCommand(data);
                        state = NORMAL;
                        break;
                }
                break;
            case AFTER_DO:
                handleDo(data);
                state = NORMAL;
                break;
            case AFTER_DONT:
                handleDont(data);
                state = NORMAL;
                break;
            case AFTER_WILL:
                handleWill(data);
                state = NORMAL;
                break;
            case AFTER_WONT:
                handleWont(data);
                state = NORMAL;
                break;
            case AFTER_SB:
                sbType = data;
                state = AFTER_SB_TYPE;
                break;
            case AFTER_SB_TYPE:
                if (data == IAC) {
                    state = AFTER_SB_IAC;
                } else {
                    sbOut.put(data);
                }
                break;
            case AFTER_SB_IAC:
                switch (data) {
                    case SE:
                        flushSuboption();
                        state = NORMAL;
                        break;
                    case IAC:
                        sbOut.put(data);
                        break;
                    default:
                        // Horrible hack assume server implementation fucked up and did not escape IAC in suboption.
                        sbOut.put(IAC);
                        sbOut.put(data);
                        break;
                }
        }
    }

    private void flushOutput() {
        byteOut.flip();
        charDecoder.decode(byteOut, charOut, false);
        byteOut.compact();
        charOut.flip();
        ansiCodec.handleRead(charOut);
        charOut.clear();
    }

    private void flushSuboption() {
        sbOut.flip();
        handleSuboption(sbType, sbOut);
        sbOut.clear();
    }

    private void handleSuboption(byte type, ByteBuffer suboption) {
        flushOutput();
        OptionHandler handler = handlers[type & 0xff];
        if (handler != null) {
            handler.handleSuboption(suboption);
        }
    }

    private void handleCommand(byte iacCommand) {
        flushOutput();
        // TODO: Handle IAC commands.
        log.info("Got IAC " + (iacCommand & 0xff));
    }

    private void handleDo(byte data) {
        flushOutput();
        OptionHandler handler = handlers[data & 0xff];
        if (handler != null) {
            handler.handleDo();
        } else {
            channelHandler.get().getWriteBuffer().put(new byte[] { IAC, WONT, data});
        }
    }

    private void handleDont(byte data) {
        flushOutput();
        OptionHandler handler = handlers[data & 0xff];
        if (handler != null) {
            handler.handleDont();
        }
    }

    private void handleWill(byte data) {
        flushOutput();
        OptionHandler handler = handlers[data & 0xff];
        if (handler != null) {
            handler.handleWill();
        } else {
            channelHandler.get().getWriteBuffer().put(new byte[] { IAC, DONT, data});
        }
    }

    private void handleWont(byte data) {
        flushOutput();
        OptionHandler handler = handlers[data & 0xff];
        if (handler != null) {
            handler.handleWont();
        }
    }
}
