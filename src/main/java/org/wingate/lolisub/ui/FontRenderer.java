package org.wingate.lolisub.ui;

import javax.swing.*;
import java.awt.*;

public class FontRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if(value instanceof Font f){
            setText(f.getFontName());
        }

        return this;
    }
}
