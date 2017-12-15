package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.*;

public class SuppOptionHandler extends AbstractOptionHandler {
    @Inject
    public SuppOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        super(handler, TOPT_SUPP, false, false, false, false);
    }

    @Override
    protected void onLocal(boolean allow) {
    }

    @Override
    protected void onRemote(boolean allow) {
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
    }
}
