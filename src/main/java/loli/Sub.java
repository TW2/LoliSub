package loli;

import com.formdev.flatlaf.FlatLightLaf;
import loli.ui.MainFrame;

import java.awt.*;
import java.util.ResourceBundle;

public class Sub {
    public static final ResourceBundle L = ResourceBundle.getBundle("L");

    private static final String NAME = "LoliSub";
    private static final String VERSION = "snapshot";
    private static final String AUTHOR = "RedDog,csa aka Chien-Rouge aka TW2 aka The Wingate 2940";
    private static final String SOURCE = "https://github.com/TW2/LoliSub";
    private static final String BLOG = "https://redaffaire.wordpress.com/";
    private static final String DISCORD = "https://discord.gg/ef8xvA9wsF";

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            System.out.printf("\\\\ %s //\n", NAME);
            System.out.printf("Version :\n%s\n", VERSION);
            System.out.printf("Proudly provided to you by :\n%s\n", AUTHOR);
            System.out.printf("Source :\n%s\n", SOURCE);
            System.out.printf("Blog :\n%s\n", BLOG);
            System.out.printf("Discord :\n%s\n", DISCORD);

            FlatLightLaf.setup();
            MainFrame mf = new MainFrame();
            mf.setTitle(String.format("%s (%s)", NAME, SOURCE));
            mf.setLocationRelativeTo(null);
            mf.setVisible(true);
        });
    }
}
