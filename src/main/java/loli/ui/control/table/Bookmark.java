package loli.ui.control.table;

import loli.enumeration.DrawColor;

import java.awt.*;

public abstract class Bookmark {
    protected int line;
    protected String name;
    protected Color bookmarkColor;

    public Bookmark(int line) {
        this.line = line;
        name = "Unknown bookmark";
        bookmarkColor = DrawColor.corn_flower_blue.getColor();
    }

    public void draw(Graphics2D g, int x, int y, int width, int height){
        g.setColor(bookmarkColor);
        g.fillRect(x, y, width, height);
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getBookmarkColor() {
        return bookmarkColor;
    }

    public void setBookmarkColor(Color bookmarkColor) {
        this.bookmarkColor = bookmarkColor;
    }
}
