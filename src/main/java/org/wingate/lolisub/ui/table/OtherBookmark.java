package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.helper.DrawColor;

public class OtherBookmark extends Bookmark {
    public OtherBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.cyan.getColor();
    }
}
