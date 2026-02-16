package loli.ui.control.table;

import loli.enumeration.DrawColor;

public class CheckBookmark extends Bookmark {
    public CheckBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.yellow.getColor();
    }
}
