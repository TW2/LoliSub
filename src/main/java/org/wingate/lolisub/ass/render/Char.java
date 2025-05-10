package org.wingate.lolisub.ass.render;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Char extends AGraphicElement {

    private String character;
    private final List<Map<Tag, String>> tags;
    private Font font;
    private double extraSpacing;

    public Char() {
        graphicType = GraphicType.Letter;
        tags = new ArrayList<>();
        font = null;
        extraSpacing = 0d;
    }

    @Override
    public void setShape(GeneralPath sh) {
        size = sh.getBounds().getSize();
        double x = sh.getBounds2D().getX();
        double y = sh.getBounds2D().getY();
        insertPoint = new Point2D.Double(x, y);
        relativeToInsertPoint = new Point2D.Double();
        shape = sh;
    }

    public void addTag(Map<Tag, String> tag){
        tags.add(tag);
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public List<Map<Tag, String>> getTags() {
        return tags;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public double getExtraSpacing() {
        return extraSpacing;
    }

    public void setExtraSpacing(double extraSpacing) {
        this.extraSpacing = extraSpacing;
    }
}
