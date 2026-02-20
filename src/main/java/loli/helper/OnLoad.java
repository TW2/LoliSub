package loli.helper;

import loli.enumeration.ISO_3166;
import loli.enumeration.ISO_639;

import javax.swing.*;
import java.util.Objects;

public class OnLoad {

    public static ImageIcon images(String imageName){
        return new ImageIcon(Objects.requireNonNull(
                OnLoad.class.getResource("/images/" + imageName)
        ));
    }

    public static ImageIcon locations(String imageName){
        return new ImageIcon(Objects.requireNonNull(
                OnLoad.class.getResource("/locations/" + imageName.toLowerCase())
        ));
    }

    public static ImageIcon locations(Object locale){
        try{
            switch(locale){
                case ISO_639 x -> {
                    String search = String.format("%s.gif", x.getSet1().toLowerCase());
                    return new ImageIcon(Objects.requireNonNull(
                            OnLoad.class.getResource("/locations/" + search)
                    ));
                }
                case ISO_3166 x -> {
                    String flag = String.format("%s.gif", x.getAlpha2().toLowerCase());
                    return new ImageIcon(Objects.requireNonNull(
                            OnLoad.class.getResource("/locations/" + flag)
                    ));
                }
                default -> {
                    // Create a false flag
                    String flag = "gb.gif";
                    return new ImageIcon(Objects.requireNonNull(
                            OnLoad.class.getResource("/locations/" + flag)
                    ));
                }
            }
        }catch(Exception _){
            // Found exceptions
            String flag = "gb.gif";
            return new ImageIcon(Objects.requireNonNull(
                    OnLoad.class.getResource("/locations/" + flag)
            ));
        }
    }
}
