package org.wingate.lolisub.helper;

import javax.swing.*;
import java.util.Objects;

public class Ico {

    public static ImageIcon images(String imageName){
        return new ImageIcon(Objects.requireNonNull(
                Ico.class.getResource("/images/" + imageName)
        ));
    }

    public static ImageIcon locations(String imageName){
        return new ImageIcon(Objects.requireNonNull(
                Ico.class.getResource("/locations/" + imageName.toLowerCase())
        ));
    }
}
