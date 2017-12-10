package org.cyberiantiger.slud.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.swing.*;

/**
 * Gather Ui components, and in the darkness bind them.
 */
public class UiImpl implements Ui {
    private static final Logger log = LoggerFactory.getLogger(UiImpl.class);
    private SludUi ui;

    @Inject
    public UiImpl(SludUi ui) {
        this.ui = ui;
    }

    @Override
    public void clear() {
        ui.getOutputField().setText("");
        log.info("UiImpl.clear()");
    }

    @Override
    public void setX(int x) {
        log.info("UiImpl.setX({})", x);
    }

    @Override
    public void moveX(int x) {
        log.info("UiImpl.moveX({})", x);
    }

    @Override
    public void setY(int y) {
        log.info("UiImpl.setY({})", y);
    }

    @Override
    public void moveY(int y) {
        log.info("UiImpl.moveY({})", y);
        JTextArea outputField = ui.getOutputField();
        String currentText = outputField.getText();
        ui.getOutputField().setText(currentText + '\n');
        JScrollBar verticalScrollBar = ui.getScrollPane().getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    @Override
    public void write(String data) {
        log.info("UiImpl.write({})", data);
        JTextArea outputField = ui.getOutputField();
        String currentText = outputField.getText();
        ui.getOutputField().setText(currentText + data);
    }

    @Override
    public void reset() {
        log.info("UiImpl.reset()");
    }

    @Override
    public void setForeground(ConsoleColor color) {
        log.info("UiImpl.setForeground({})", color);
    }

    @Override
    public void setBackground(ConsoleColor color) {
        log.info("UiImpl.setBackground({})", color);
    }

    @Override
    public void setBold(boolean bold) {
        log.info("UiImpl.setBold({})", bold);
    }

    @Override
    public void setFlash(boolean flash) {
        log.info("UiImpl.setFlash({})", flash);
    }

    @Override
    public void setItalic(boolean italic) {
        log.info("UiImpl.setItalic({})", italic);
    }

    @Override
    public void setUnderline(boolean underline) {
        log.info("UiImpl.setUnderline({})", underline);
    }

    @Override
    public void reverse() {
        log.info("UiImpl.reverse()");
    }

    @Override
    public void resetAttributes() {
        log.info("UiImpl.resetAttributes()");
    }

    @Override
    public void beep() {
        log.info("UiImpl.beep()");
    }

    @Override
    public void setConnectionStatus(ConnectionStatus status) {
        log.info("UiImpl.setConnectionStatus({})", status);
    }
}
