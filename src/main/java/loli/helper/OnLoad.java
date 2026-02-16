package loli.helper;

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
}
