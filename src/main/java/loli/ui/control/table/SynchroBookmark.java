package loli.ui.control.table;

import loli.enumeration.DrawColor;

public class SynchroBookmark extends Bookmark {
    public SynchroBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.magenta.getColor();
    }
}
