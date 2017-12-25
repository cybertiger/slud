package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.TextColor;
import lombok.Getter;
import org.cyberiantiger.slud.model.*;
import org.cyberiantiger.slud.net.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Ui extends GmcpHandler, Terminal {
    enum ConnectionStatus {
        DISCONNECTED(IconType.DISCONNECTED),
        CONNECTING(IconType.CONNECTING) {
            public void action(Network net) {
                net.disconnect();
            }
        },
        CONNECTED(IconType.CONNECTED) {
            public void action(Network net) {
                net.disconnect();
            }
        };
        private static final Logger log = LoggerFactory.getLogger(Ui.class);

        @Getter
        private final IconType iconType;

        ConnectionStatus(IconType iconType) {
            this.iconType = iconType;
        }

        public void action(Network net) {
            net.connect("elephant.org", 23, "ANSI");
        }
    }

    /**
     * Set connection status.
     */
    void setConnectionStatus(ConnectionStatus status);

}
