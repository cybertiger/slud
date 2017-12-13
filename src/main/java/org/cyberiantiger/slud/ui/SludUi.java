package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.swing.*;
import dagger.Lazy;
import lombok.Getter;
import org.cyberiantiger.slud.Slud;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class SludUi {
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
        outputField.setFocusable(false);
        outputField.setPreferredSize(new Dimension(800, 600));
        outputField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        connectButton.setFocusable(false);
        mainFrame.pack();
        // Actions.
        connectButton.addActionListener((action) -> main.runInNetwork((net) -> {
            // TODO: set width/height appropriately.
            net.connect("elephant.org", 23, "ANSI", 80, 50);
        }));
        inputField.addActionListener(action -> {
            String text = inputField.getText();
            inputField.selectAll();
            ui.get().localEcho(text);
            main.runInNetwork(net -> net.sendCommand(text));
        });
    }
}
