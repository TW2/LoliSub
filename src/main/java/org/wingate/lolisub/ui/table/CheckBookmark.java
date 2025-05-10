package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.helper.DrawColor;

public class CheckBookmark extends Bookmark {
    public CheckBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.yellow.getColor();
    }
}
