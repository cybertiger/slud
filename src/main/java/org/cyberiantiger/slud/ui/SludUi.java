package org.cyberiantiger.slud.ui;

import lombok.Getter;
import org.cyberiantiger.slud.Slud;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class SludUi {
    Slud main;
    JFrame mainFrame = new JFrame();
    JTextField inputField = new JTextField();
    @Getter
    JTextArea outputField = new JTextArea();
    JPanel bottomPanel = new JPanel();
    @Getter
    JButton connectButton = new JButton("Connect");
    @Getter
    JScrollPane scrollPane = new JScrollPane();

    @Inject
    public SludUi(Slud main) {
        this.main = main;
        initComponents();
        mainFrame.setVisible(true);
    }

    private void initComponents() {
        // Layout.
        // TODO: save/restore layout from config.
        JRootPane rootPane = mainFrame.getRootPane();
        rootPane.setLayout(new BorderLayout());
        rootPane.add(scrollPane, BorderLayout.CENTER);
        rootPane.add(bottomPanel, BorderLayout.SOUTH);
        scrollPane.getViewport().setLayout(new BorderLayout());
        scrollPane.getViewport().add(outputField, BorderLayout.CENTER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
            main.runInNetwork(net -> net.sendCommand(text));
        });
    }
}
