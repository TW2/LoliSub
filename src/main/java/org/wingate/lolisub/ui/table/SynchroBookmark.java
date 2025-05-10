package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.helper.DrawColor;

public class SynchroBookmark extends Bookmark {
    public SynchroBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.magenta.getColor();
    }
}
