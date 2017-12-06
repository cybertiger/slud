package org.cyberiantiger.slud;

import org.cyberiantiger.slud.net.Backend;
import org.cyberiantiger.slud.net.TelnetSocketChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Slud entry point.
 */
public class Slud implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Slud.class);

    public static void main(String... args) {
        new Slud().run();
    }

    @Override
    public void run() {
        SludComponent main = DaggerSludComponent.create();
        Backend backend = main.getBackend();
        backend.start();
        try {
            TelnetSocketChannelHandler handler = new TelnetSocketChannelHandler();
            backend.addTask(() -> {
                try {
                    backend.register(handler);
                    handler.getChannel().connect(new InetSocketAddress("elephant.org", 23));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(() -> {
                try {
                    while (handler.getChannel().isOpen()) {
                        byte[] data = new byte[2048];
                        int len = System.in.read(data, 0, data.length);
                        if (len < 0) {
                            return;
                        }
                        backend.addTask(() -> {
                            handler.getWriteBuffer().put(data, 0, len);
                        });
                    }
                    backend.addTask(() -> {
                        try {
                            backend.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
