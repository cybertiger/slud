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
import org.cyberiantiger.slud.ui.model.AvatarExperience;
import org.cyberiantiger.slud.ui.model.AvatarVital;
import org.cyberiantiger.slud.ui.model.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@Singleton
public class SludUi {
    private static final Logger log = LoggerFactory.getLogger(Logger.class);
    private final Lazy<Ui> ui;
    private final Slud main;
    private final Avatar avatar;
    private final ImageCache cache;
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
    public SludUi(Slud main, Lazy<Ui> ui, Avatar avatar, ImageCache cache) {
        this.main = main;
        this.ui = ui;
        this.avatar = avatar;
        this.cache = cache;
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
        initComponents();
        setConnectionStatus(Ui.ConnectionStatus.DISCONNECTED);
        mainFrame.setVisible(true);
        gaugeDialog.setVisible(true);
    }


    private SkinnableGauge createGauge(Color gaugeColor, Color changeColor) {
        SkinnableGauge result = new SkinnableGauge(
                cache,
                IconType.GAUGE_BASE,
                IconType.GAUGE_GAUGE,
                IconType.GAUGE_OVERLAY,
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
        spBar = createGauge(Color.YELLOW, Color.YELLOW.darker().darker());
        xpBar = createGauge(Color.WHITE, Color.WHITE.darker().darker());
        gaugeDialog.getRootPane().add(hpBar, getGridBagConstraints(0, 0));
        gaugeDialog.getRootPane().add(mpBar, getGridBagConstraints(0, 1));
        gaugeDialog.getRootPane().add(spBar, getGridBagConstraints(0, 2));
        gaugeDialog.getRootPane().add(xpBar, getGridBagConstraints(0, 3));
        gaugeDialog.pack();
        avatar.getHp().addChangeListener(new VitalUpdater("HP", hpBar));
        avatar.getMp().addChangeListener(new VitalUpdater("MP", mpBar));
        avatar.getSp().addChangeListener(new VitalUpdater("SP", spBar));
        avatar.getXp().addChangeListener(new ExperienceUpdater(xpBar));
    }

    private GridBagConstraints getGridBagConstraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        return constraints;
    }

    public void setConnectionStatus(Ui.ConnectionStatus status) {
        this.status = status;
        connectButton.setIcon(status.getIconType().get());
    }

    private void onConnectClick(ActionEvent event) {
        Ui.ConnectionStatus status = this.status;
        main.runInNetwork(status::action);
    }

    private static class VitalUpdater implements ChangeListener<AvatarVital> {
        private final String name;
        private final SkinnableGauge gauge;

        public VitalUpdater(String name, SkinnableGauge gauge) {
            this.name = name;
            this.gauge = gauge;
        }

        @Override
        public void stateChanged(AvatarVital state) {
            int value = state.getValue();
            int max = state.getMax();
            if (max <= 0) {
                gauge.setValue(0);
                gauge.setToolTipText(null);
            } else {
                if (value < 0) {
                    value = 0;
                } else if (value >= max) {
                    value = max;
                }
                gauge.setValue(1f * value / max);
                gauge.setToolTipText(String.format("%s: %d / %d", name, value, max));
            }
        }
    }

    private static class ExperienceUpdater implements ChangeListener<AvatarExperience> {
        private SkinnableGauge gauge;

        public ExperienceUpdater(SkinnableGauge gauge) {
            this.gauge = gauge;
        }

        @Override
        public void stateChanged(AvatarExperience state) {
            long value = state.getValue();
            long min = state.getMin();
            long max = state.getMax();
            // Missing state information, give up and show nothing.
            if (max <= 0 || min >= max) {
                gauge.setValue(0);
                gauge.setToolTipText(null);
                return;
            }
            long clippedValue = value > max ? max : value;
            float gaugePercent = (clippedValue - min) / (max - min);
            log.info("setting xp gauge to " + gaugePercent);
            gauge.setValue(gaugePercent);
            StringBuilder tooltip =
                    new StringBuilder("<html> XP: ").append(value);
            if (min > 0) {
                float minBuffer = 1f - ((float) min / value);
                tooltip.append(String.format("<br>Current XP buffer: %.2f%%", minBuffer * 100));
            }
            if (value > max) {
                float nextBuffer = 1f - ((float) max / value);
                tooltip.append(String.format("<br>Next level XP buffer: %.2f%%", nextBuffer * 100));
            }
            tooltip.append("</html>");
            gauge.setToolTipText(tooltip.toString());
        }
    }
}
