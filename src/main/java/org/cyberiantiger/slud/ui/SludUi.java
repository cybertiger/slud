package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorColorConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorDeviceConfiguration;
import dagger.Lazy;
import lombok.Getter;
import org.cyberiantiger.slud.Slud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class SludUi {
    private static final Logger log = LoggerFactory.getLogger(Logger.class);
    private final Lazy<Ui> ui;
    private final Slud main;
    JFrame mainFrame = new JFrame();
    JTextField inputField = new JTextField();
    @Getter
    ScrollingSwingTerminal outputField;
    JPanel bottomPanel = new JPanel();
    @Getter
    JButton connectButton = new JButton("Connect");

    @Inject
    public SludUi(Slud main, Lazy<Ui> ui) {
        this.main = main;
        this.ui = ui;
        TerminalEmulatorDeviceConfiguration termConfig =
                new TerminalEmulatorDeviceConfiguration(
                        50000, // Scrollback buffer
                        500, // Blink period millis
                        TerminalEmulatorDeviceConfiguration.CursorStyle.VERTICAL_BAR, // cursor style
                        TextColor.ANSI.WHITE,
                        false,
                        true);
        SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.getDefault();
        TerminalEmulatorColorConfiguration colorConfig = TerminalEmulatorColorConfiguration.getDefault();
        outputField = new ScrollingSwingTerminal(termConfig, fontConfig, colorConfig);
        ScrollingSwingTerminal asdf;
        initComponents();
        mainFrame.setVisible(true);
    }

    private void initComponents() {
        // Layout.
        // TODO: save/restore layout from config.
        JRootPane rootPane = mainFrame.getRootPane();
        rootPane.setLayout(new BorderLayout());
        rootPane.add(outputField, BorderLayout.CENTER);
        rootPane.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(connectButton, BorderLayout.EAST);
        // outputField.setFocusable(false);
        outputField.addResizeListener((terminal, terminalSize) -> {
            log.info("Setting terminal size to: " + terminalSize);
            main.runInNetwork(network -> network.setTerminalSize(terminalSize.getRows(), terminalSize.getColumns()));
        });
        outputField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        connectButton.setFocusable(false);
        mainFrame.pack();
        // Actions.
        connectButton.addActionListener((action) -> main.runInNetwork((net) -> {
            // TODO: set width/height appropriately.
            net.connect("elephant.org", 23, "ANSI");
        }));
        inputField.addActionListener(action -> {
            String text = inputField.getText();
            inputField.selectAll();
            ui.get().localEcho(text);
            main.runInNetwork(net -> net.sendCommand(text));
        });
    }
}
