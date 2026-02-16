package loli.helper;

import loli.Sub;

import javax.swing.*;

public class OnError {
    private static String filter(String s){
        return s.replace("[SPACE]", " ");
    }

    public static void consoleErr(String source){
        String startTranslation = filter(Sub.L.getString("errMsg"));
        System.err.printf("%s:\n%s\n", startTranslation, source);
    }

    public static void dialogErr(String source){
        consoleErr(source);
        String startTranslation = filter(Sub.L.getString("errMsg"));
        String title = Sub.L.getString("errMsgTitle");

        JOptionPane.showMessageDialog(
                new javax.swing.JFrame(),
                String.format("%s:\n%s\n", startTranslation, source),
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

}
