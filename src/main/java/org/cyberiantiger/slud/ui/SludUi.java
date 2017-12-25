package org.cyberiantiger.slud.ui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorColorConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorDeviceConfiguration;
import dagger.Lazy;
import lombok.Getter;
import org.cyberiantiger.slud.Slud;
import org.cyberiantiger.slud.ui.component.SkinnableGauge;
import org.cyberiantiger.slud.ui.model.Avatar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@Singleton
public class SludUi {
    private static final Logger log = LoggerFactory.getLogger(Logger.class);
    private final Lazy<Ui> ui;
    private final Slud main;
    private final Avatar avatar;
    private final Map<IconType, ImageIcon> icons = new EnumMap<>(IconType.class);
    private Ui.ConnectionStatus status;
    JFrame mainFrame;
    JTextField inputField;
    @Getter
    ScrollingSwingTerminal outputField;
    JPanel bottomPanel;
    @Getter
    JButton connectButton;
    JDialog gaugeDialog;
    @Getter
    SkinnableGauge hpBar;
    @Getter
    SkinnableGauge mpBar;
    @Getter
    SkinnableGauge spBar;
    @Getter
    SkinnableGauge xpBar;

    @Inject
    public SludUi(Slud main, Lazy<Ui> ui, Avatar avatar) {
        this.main = main;
        this.ui = ui;
        this.avatar = avatar;
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
        gaugeDialog.setVisible(true);
    }

    private void loadIcons() {
        for (IconType type : IconType.values()) {
            icons.put(type, type.load());
        }
    }

    private SkinnableGauge createGauge(Color gaugeColor, Color changeColor) {
        SkinnableGauge result = new SkinnableGauge(
                icons.get(IconType.GAUGE_BASE),
                icons.get(IconType.GAUGE_GAUGE),
                icons.get(IconType.GAUGE_OVERLAY),
                gaugeColor,
                changeColor,
                8, 248, true);
        result.setForeground(Color.WHITE.darker());
        return result;
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
        outputField.setFocusable(false);
        connectButton.setFocusable(false);
        mainFrame.pack();
        // Actions.
        connectButton.addActionListener(this::onConnectClick);
        inputField.addActionListener(action -> {
            String text = inputField.getText();
            inputField.selectAll();
            if ("!showavatar".equals(text)) {
                log.info("Avatar is\n {}", avatar);
            }
            ui.get().localEcho(text);
            main.runInNetwork(net -> net.sendCommand(text));
        });

        // Gauges
        gaugeDialog = new JDialog(mainFrame);
        gaugeDialog.getRootPane().setLayout(new GridBagLayout());
        gaugeDialog.setBackground(Color.BLACK);
        hpBar = createGauge(Color.RED, Color.RED.darker().darker());
        mpBar = createGauge(Color.BLUE, Color.BLUE.darker().darker());
        spBar = createGauge(Color.YELLOW.darker(), Color.YELLOW.darker().darker().darker());
        xpBar = createGauge(Color.WHITE.darker().darker(), Color.WHITE.darker().darker().darker().darker());
        gaugeDialog.getRootPane().add(hpBar, getGridBagConstraints(0, 0));
        gaugeDialog.getRootPane().add(mpBar, getGridBagConstraints(0, 1));
        gaugeDialog.getRootPane().add(spBar, getGridBagConstraints(0, 2));
        gaugeDialog.getRootPane().add(xpBar, getGridBagConstraints(0, 3));
        gaugeDialog.pack();
    }

    private GridBagConstraints getGridBagConstraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        return constraints;
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
