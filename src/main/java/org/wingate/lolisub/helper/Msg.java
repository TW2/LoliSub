package org.wingate.lolisub.helper;

import org.wingate.lolisub.LoliSub;

import javax.swing.*;

public class Msg {

    private static String filter(String s){
        return s.replace("[SPACE]", " ");
    }

    public static void consoleErr(String source){
        String startTranslation = filter(LoliSub.RSX.getString("errMsg"));
        System.err.printf("%s:\n%s\n", startTranslation, source);
    }

    public static void dialogErr(String source){
        consoleErr(source);
        String startTranslation = filter(LoliSub.RSX.getString("errMsg"));
        String title = LoliSub.RSX.getString("errMsgTitle");

        JOptionPane.showMessageDialog(
                new javax.swing.JFrame(),
                String.format("%s:\n%s\n", startTranslation, source),
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

}
