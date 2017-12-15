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
import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SludUi {
    private static final Logger log = LoggerFactory.getLogger(Logger.class);
    private final Lazy<Ui> ui;
    private final Slud main;
    private final Map<IconType, ImageIcon> icons = new EnumMap<>(IconType.class);
    private Ui.ConnectionStatus status;
    JFrame mainFrame;
    JTextField inputField;
    @Getter
    ScrollingSwingTerminal outputField;
    JPanel bottomPanel;
    @Getter
    JButton connectButton;

    public enum IconType {
        CONNECTED("connected.png"),
        CONNECTING("connecting.gif"), // Animated.
        DISCONNECTED("disconnected.png");

        @Getter
        private String resource;

        IconType(String resource) {
            this.resource = resource;
        }

        public ImageIcon load() {
            return new ImageIcon(SludUi.class.getResource(resource));
        }
    }

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
                        false);
        SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.getDefault();
        TerminalEmulatorColorConfiguration colorConfig = TerminalEmulatorColorConfiguration.getDefault();
        outputField = new ScrollingSwingTerminal(termConfig, fontConfig, colorConfig);
        loadIcons();
        initComponents();
        setConnectionStatus(Ui.ConnectionStatus.DISCONNECTED);
        mainFrame.setVisible(true);
    }

    private void loadIcons() {
        for (IconType type : IconType.values()) {
            icons.put(type, type.load());
        }
    }

    private void initComponents() {
        mainFrame = new JFrame();
        inputField = new JTextField();
        bottomPanel = new JPanel();
        connectButton = new JButton();
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE); // TODO: Cleanup shutdown logic.
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
        connectButton.addActionListener(this::onConnectClick);
        inputField.addActionListener(action -> {
            String text = inputField.getText();
            inputField.selectAll();
            ui.get().localEcho(text);
            main.runInNetwork(net -> net.sendCommand(text));
        });
    }

    public void setConnectionStatus(Ui.ConnectionStatus status) {
        this.status = status;
        connectButton.setIcon(icons.get(status.getIconType()));
    }

    private void onConnectClick(ActionEvent event) {
        Ui.ConnectionStatus status = this.status;
        main.runInNetwork(status::action);
    }
}
