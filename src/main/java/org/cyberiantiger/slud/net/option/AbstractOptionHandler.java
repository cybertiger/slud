package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import lombok.Getter;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.*;

/**
 * Base class for option handlers.
 *
 * Whoever designed telnet option handing is some sort of masochist.
 */
public abstract class AbstractOptionHandler implements OptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Lazy<TelnetSocketChannelHandler> handler;
    private final byte option;
    private final boolean initLocal;
    private final boolean initRemote;
    private final boolean acceptLocal;
    private final boolean acceptRemote;
    @Getter
    private boolean local = false;
    @Getter
    private boolean remote = false;

    public AbstractOptionHandler(Lazy<TelnetSocketChannelHandler> handler,
                                 byte option,
                                 boolean initLocal,
                                 boolean initRemote,
                                 boolean acceptLocal,
                                 boolean acceptRemote) {
        this.handler = handler;
        this.option = option;
        this.initLocal = initLocal;
        this.initRemote = initRemote;
        this.acceptLocal = acceptLocal;
        this.acceptRemote = acceptRemote;
    }

    protected abstract void onLocal(boolean allow);
    protected abstract void onRemote(boolean allow);

    protected TelnetSocketChannelHandler getHandler() {
        return handler.get();
    }

    @Override
    public final byte getOption() {
        return option;
    }

    @Override
    public final void handleConnect() {
        if (initLocal) {
            if (acceptLocal) {
                sendWill();
            } else {
                sendWont();
            }
        }
        if (initRemote) {
            if (acceptRemote) {
                sendDo();
            } else {
                sendDont();
            }
        }
    }

    @Override
    public final void handleDo() {
        log.trace("RECV: DO {}", option & 0xff);
        if (acceptLocal) {
            if (!initLocal) {
                sendWill();
            }
            local = true;
            onLocal(true);
        } else {
            if (!initLocal) {
                sendWont();
            }
            local = false;
            onLocal(false);
        }
    }

    @Override
    public final void handleDont() {
        log.trace("RECV: DONT {}", option & 0xff);
        if (!initLocal) {
            sendWont();
        }
        local = false;
        onLocal(false);
    }

    @Override
    public final void handleWill() {
        log.trace("RECV: WILL {}", option & 0xff);
        if (acceptRemote) {
            if (!initRemote) {
                sendDo();
            }
            remote = true;
            onRemote(true);
        } else {
            if (!initRemote) {
                sendWont();
            }
            remote = false;
            onRemote(false);
        }
    }

    @Override
    public final void handleWont() {
        log.trace("RECV: WONT {}", option & 0xff);
        if (!initRemote) {
            sendDont();
        }
        remote = false;
        onRemote(false);
    }

    public void sendSuboption(ByteBuffer data) {
        if(local) {
            log.trace("SEND: SB {} <data>", option & 0xff);
            ByteBuffer writeBuffer = this.handler.get().getWriteBuffer();
            writeBuffer.put(IAC);
            writeBuffer.put(SB);
            writeBuffer.put(option);
            while (data.hasRemaining()) {
                byte b = data.get();
                if (b == IAC) {
                    writeBuffer.put(IAC);
                }
                writeBuffer.put(b);
            }
            writeBuffer.put(IAC);
            writeBuffer.put(SE);
        }
    }

    private void sendWill() {
        log.trace("SEND: WILL {}", option & 0xff);
        ByteBuffer writeBuffer = this.handler.get().getWriteBuffer();
        writeBuffer.put(IAC);
        writeBuffer.put(WILL);
        writeBuffer.put(option);
    }

    private void sendWont() {
        log.trace("SEND: WONT {}", option & 0xff);
        ByteBuffer writeBuffer = this.handler.get().getWriteBuffer();
        writeBuffer.put(IAC);
        writeBuffer.put(WONT);
        writeBuffer.put(option);
    }

    private void sendDo() {
        log.trace("SEND: DO {}", option & 0xff);
        ByteBuffer writeBuffer = this.handler.get().getWriteBuffer();
        writeBuffer.put(IAC);
        writeBuffer.put(DO);
        writeBuffer.put(option);
    }

    private void sendDont() {
        log.trace("SEND: DONT {}", option & 0xff);
        ByteBuffer writeBuffer = this.handler.get().getWriteBuffer();
        writeBuffer.put(IAC);
        writeBuffer.put(DONT);
        writeBuffer.put(option);
    }
}
