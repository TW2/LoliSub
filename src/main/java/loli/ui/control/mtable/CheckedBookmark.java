package loli.ui.control.mtable;

import loli.enumeration.DrawColor;

public class CheckedBookmark extends Bookmark {
    CheckedBookmark(String name) {
        super(name);
        color = DrawColor.gold.getColor();
    }
}
