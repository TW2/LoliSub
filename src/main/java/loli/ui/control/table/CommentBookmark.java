package loli.ui.control.table;

import loli.enumeration.DrawColor;

public class CommentBookmark extends Bookmark {
    public CommentBookmark(int line) {
        super(line);
        bookmarkColor = DrawColor.lime_green.getColor();
    }
}
