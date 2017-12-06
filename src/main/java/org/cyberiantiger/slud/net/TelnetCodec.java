package org.cyberiantiger.slud.net;

import lombok.Getter;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetCodec.TelnetState.*;
import static org.cyberiantiger.slud.net.TelnetOption.*;

public class TelnetCodec {
    private static final Logger log = LoggerFactory.getLogger(TelnetCodec.class);
    @Getter
    private TelnetSocketChannelHandler channelHandler;
    private byte sbType;
    private ByteBuffer sbOut;
    private ByteBuffer byteOut;
    private CharBuffer charOut;
    private CharsetDecoder charDecoder = StandardCharsets.UTF_8.newDecoder();
    private TelnetState state = NORMAL;
    private AnsiCodec ansiCodec = new AnsiCodec();
    private OptionHandler[] handlers = new OptionHandler[256];
    public interface OptionHandler {
        void handleConnect();
        void handleDo();
        void handleDont();
        void handleWill();
        void handleWont();
        void handleSuboption(ByteBuffer data);
    }

    @Value
    private class DefaultOptionHandler implements OptionHandler {
        private byte type;

        @Override
        public void handleConnect() {
            // NOOP
        }

        @Override
        public void handleDo() {
            log.info("IAC DO {}", type & 0xff);
            channelHandler.getWriteBuffer().put(new byte[] { IAC, WONT, type});
        }

        @Override
        public void handleDont() {
            log.info("IAC DONT {}", type & 0xff);
            channelHandler.getWriteBuffer().put(new byte[] { IAC, WONT, type});
        }

        @Override
        public void handleWill() {
            log.info("IAC WILL {}", type & 0xff);
            channelHandler.getWriteBuffer().put(new byte[] { IAC, DONT, type});
        }

        @Override
        public void handleWont() {
            log.info("IAC WONT {}", type & 0xff);
            channelHandler.getWriteBuffer().put(new byte[] { IAC, DONT, type});
        }

        @Override
        public void handleSuboption(ByteBuffer data) {
            log.info("IAC SB {} IAC SE", type & 0xff);
        }
    }

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

    public TelnetCodec(TelnetSocketChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
        // Allocate some fixed size buffers....
        sbOut = ByteBuffer.allocate(0x100000);
        byteOut = ByteBuffer.allocate(0x100000);
        charOut = CharBuffer.allocate(0x100000);
        for (int i = 0; i < 256; i++) {
            handlers[i] = new DefaultOptionHandler((byte)i);
        }
        handlers[TOPT_GMCP & 0xff] = new GmcpOptionHandler(this);
    }

    public void handleRead(ByteBuffer input) {
        while (input.hasRemaining()) {
            handleByte(input.get());
        }
        flushOutput();
    }

    public void handleConnect() {
        for (OptionHandler handler : handlers) {
            handler.handleConnect();
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
        // TODO: Pass to ANSI Decoder.
        ansiCodec.handleRead(charOut);
        charOut.clear();
    }

    private void flushSuboption() {
        flushOutput();
        sbOut.flip();
        handlers[sbType & 0xff].handleSuboption(sbOut);
        sbOut.clear();
    }

    private void handleCommand(byte iacCommand) {
        flushOutput();
        log.info("Got IAC " + (iacCommand & 0xff));
    }

    private void handleDo(byte data) {
        flushOutput();
        handlers[data & 0xff].handleDo();
    }

    private void handleDont(byte data) {
        flushOutput();
        handlers[data & 0xff].handleDont();
    }

    private void handleWill(byte data) {
        flushOutput();
        handlers[data & 0xff].handleWill();
    }

    private void handleWont(byte data) {
        flushOutput();
        handlers[data & 0xff].handleWont();
    }
}
