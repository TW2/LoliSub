package loli.ui.control.mtable;

import loli.enumeration.DrawColor;

public class TranslatedBookmark extends Bookmark {
    TranslatedBookmark(String name) {
        super(name);
        color = DrawColor.deep_pink.getColor();
    }
}
