package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class TermOptionHandler extends AbstractOptionHandler {
    private static final Logger log = LoggerFactory.getLogger(TermOptionHandler.class);
    private byte[] term;

    @Inject
    public TermOptionHandler(Lazy<TelnetSocketChannelHandler> handler, @Named("term") String term) {
        super(handler, TOPT_TERM, true, false, true, false);
        this.term = term.getBytes(StandardCharsets.UTF_8);
        byte[] termBytes = term.getBytes(StandardCharsets.UTF_8);
        this.term = new byte[termBytes.length + 1];
        this.term[0] = IS;
        System.arraycopy(termBytes, 0, this.term, 1, termBytes.length);
    }

    @Override
    protected void onLocal(boolean allow) {
        if (allow) {
            sendSuboption(ByteBuffer.wrap(term));
        }
    }

    @Override
    protected void onRemote(boolean allow) {
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
    }
}
