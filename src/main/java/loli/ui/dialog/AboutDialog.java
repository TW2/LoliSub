package loli.ui.dialog;

import loli.Sub;
import loli.helper.OnLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutDialog extends JDialog {

    private final Frame owner;

    public AboutDialog(Frame owner) {
        super(owner, true);
        this.owner = owner;
        setTitle(Sub.L.getString("aboutTitle"));

        JLabel lblImage = new JLabel(OnLoad.images("logo.png"));
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
