package loli.ui.control.table;

import loli.enumeration.DrawColor;
import loli.subtitle.Event;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * A group of lines can be foldable or not
 */
public class CollapseGroup {
    private int fromLine;
    private List<Event> events;
    private String groupName;
    private Color collapsedColor;
    private Color innerCircleColor;
    private Color innerPlusColor;

    public CollapseGroup(int fromLine, List<Event> events) {
        this.fromLine = fromLine;
        this.events = events;
        groupName = "Unknown Group";
        collapsedColor = DrawColor.green_yellow.getColor();
        innerCircleColor = Color.white;
        innerPlusColor = Color.black;
    }

    public void draw(Graphics2D g, int lineHeight, int yOffset){

        float xe1 = 80f - lineHeight + 2f;
        float ye1 = (lineHeight * fromLine) - (lineHeight / 2f) + 2f;
        float ze1 = lineHeight - 4f;

        Ellipse2D outerCircle = new Ellipse2D.Float(xe1, ye1 - yOffset, ze1, ze1);

        float xe2 = xe1 + 2f;
        float ye2 = ye1 + 2f;
        float ze2 = ze1 - 4f;

        Ellipse2D innerCircle = new Ellipse2D.Float(xe2, ye2 - yOffset, ze2, ze2);

        float xr1 = xe2 + 5f;
        float yr1 = ye2 + 1.5f;
        float wr1 = 2f;
        float hr1 = ze2 - 3f;

        Rectangle2D plus1 = new Rectangle2D.Float(xr1, yr1 - yOffset, wr1, hr1);

        float xr2 = xe2 + 1.5f;
        float yr2 = ye2 + 5f;
        float wr2 = ze2 - 3f;
        float hr2 = 2f;

        Rectangle2D plus2 = new Rectangle2D.Float(xr2, yr2 - yOffset, wr2, hr2);

        g.setColor(collapsedColor);
        g.fill(outerCircle);

        g.setColor(innerCircleColor);
        g.fill(innerCircle);

        g.setColor(innerPlusColor);
        g.fill(plus1);
        g.fill(plus2);
    }

    public int getFromLine() {
        return fromLine;
    }

    public void setFromLine(int fromLine) {
        this.fromLine = fromLine;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getItemsCount(){
        return events.size();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCollapsedColor(Color collapsedColor) {
        this.collapsedColor = collapsedColor;
    }

    public void setInnerCircleColor(Color innerCircleColor) {
        this.innerCircleColor = innerCircleColor;
    }

    public void setInnerPlusColor(Color innerPlusColor) {
        this.innerPlusColor = innerPlusColor;
    }
}
