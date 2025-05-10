package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.MainFrame;

import javax.swing.*;

public class ChatPanel extends JPanel {

    private final MainFrame mainFrame;

    public ChatPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
