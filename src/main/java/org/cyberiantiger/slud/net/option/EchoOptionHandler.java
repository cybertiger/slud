package org.cyberiantiger.slud.net.option;

import dagger.Lazy;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static org.cyberiantiger.slud.net.TelnetOption.TOPT_ECHO;

public class EchoOptionHandler extends AbstractOptionHandler {
    @Inject
    public EchoOptionHandler(Lazy<TelnetSocketChannelHandler> handler) {
        super(handler, TOPT_ECHO, false, false, false, true);
    }

    @Override
    protected void onLocal(boolean allow) {
    }

    @Override
    protected void onRemote(boolean allow) {
        getHandler().addUiAction(ui -> ui.setLocalEcho(!allow));
    }

    @Override
    public void handleSuboption(ByteBuffer data) {
        // ignored
    }
}
