package loli.ui.control.table;


import loli.enumeration.DrawColor;

public class OtherBookmark extends Bookmark {
    public OtherBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.cyan.getColor();
    }
}
