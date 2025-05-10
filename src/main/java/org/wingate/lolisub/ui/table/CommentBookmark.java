package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.helper.DrawColor;

public class CommentBookmark extends Bookmark {
    public CommentBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.lime_green.getColor();
    }
}
