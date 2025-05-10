package org.wingate.lolisub.dialog;

import org.wingate.lolisub.LoliSub;
import org.wingate.lolisub.helper.Ico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutDialog extends JDialog {

    private final Frame owner;

    public AboutDialog(Frame owner) {
        super(owner, true);
        this.owner = owner;
        setTitle(LoliSub.RSX.getString("aboutTitle"));

        JLabel lblImage = new JLabel(Ico.images("logo.png"));
        lblImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                setVisible(false);
                dispose();
            }
        });

        getContentPane().add(lblImage);
        pack();
    }

    public void showDialog(){
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
