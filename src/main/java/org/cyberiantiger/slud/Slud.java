package org.cyberiantiger.slud;

import org.cyberiantiger.slud.net.Network;
import org.cyberiantiger.slud.ui.Ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

/**
 * Slud entry point.
 */
public class Slud implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Slud.class);

    public static void main(String... args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Install UI skin.
        // UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        new Slud().run();
    }

    private SludComponent main;
    private Network network;
    private Ui ui;

    @Override
    public void run() {
        main = DaggerSludComponent.builder()
                .sludModule(new SludModule(this))
                .build();
        this.ui = main.getUi();
        this.network = main.getNetwork();
    }

    public void runInNetwork(Consumer<Network> task) {
        runInNetwork(singletonList(task));
    }

    public void runInNetwork(List<Consumer<Network>> tasks) {
        if (Thread.currentThread() == main.getBackend()) {
            Network network = main.getNetwork();
            for (Consumer<Network> task : tasks) {
                task.accept(network);
            }
        } else {
            @SuppressWarnings("unchecked")
            List<Consumer<Network>> tasksCopy = Arrays.asList(tasks.toArray(new Consumer[tasks.size()]));
            main.getBackend().addTask(() -> runInNetwork(tasksCopy));
        }
    }

    public void runInUi(Consumer<Ui> task) {
        runInUi(singletonList(task));
    }

    public void runInUi(List<Consumer<? super Ui>> tasks) {
        if (SwingUtilities.isEventDispatchThread()) {
            Ui ui = main.getUi();
            for (Consumer<? super Ui> task : tasks) {
                task.accept(ui);
            }
            ui.flush();
        } else {
            @SuppressWarnings("unchecked")
            List<Consumer<? super Ui>> tasksCopy = Arrays.asList(tasks.toArray(new Consumer[tasks.size()]));
            SwingUtilities.invokeLater(() -> runInUi(tasksCopy));
        }
    }
}
