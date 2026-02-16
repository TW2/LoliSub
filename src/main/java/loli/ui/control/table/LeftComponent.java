package loli.ui.control.table;

import loli.subtitle.Event;

import javax.swing.*;
import java.awt.*;

public class LeftComponent extends JPanel {
    private final MainView view;
    private int yOffset;

    public LeftComponent(MainView view) {
        this.view = view;
        yOffset = 0;
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(80, view.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(UIManager.getColor("Table.background"));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(UIManager.getColor("Table.foreground"));
        g2d.setFont(view.getTable().getAssTable().getFont());
        int lineHeight = view.getTable().getAssTable().getRowHeight();

        // n >> offset index for previous elements
        int n = yOffset / lineHeight;

        // Prefill line number for each line (recursively)
        // Draw line numbers
        int ya = -lineHeight, k = 0, m = 0;
        int hackline = 1; // Fix hacking line number of bottom
        // Set foreground color to display line number and draw
        g2d.setColor(UIManager.getColor("Table.foreground"));
        for(int i=n; i<view.getTable().getAssTableModel().getRowCount()-n; i++){
            g2d.drawString(String.format("%05d", k + i), 2, ya + lineHeight - 4);
            ya += lineHeight;

            hackline = k + i + 1; // Fix hacking line number of bottom

            Event event = (Event)view.getTable().getAssTableModel()
                    .getValueAt(i, 12);
            k += getCollapsedGroupLinesCount(event, 0);
        }
        // Fix hacking line number of bottom
        g2d.drawString(String.format("%05d", hackline), 2, ya + lineHeight - 4);

        // i >> table index for visible elements
        // y >> index for drawing line numbers and other stuffs
        for(int i=0, y=0;
            i<view.getTable().getAssTableModel().getRowCount();
            i++, y+=lineHeight){

            // AssEvent in table (visible element)
            Event event = (Event)view.getTable().getAssTableModel()
                    .getValueAt(i, 12);

//            // Draw bookmarks (visible elements)
//            int bookmarkUnits = 0;
//            for(Bookmark b : event.getBookmarks()){
//                b.draw(g2d, 34 + bookmarkUnits, y - yOffset, 4, lineHeight);
//                bookmarkUnits += 6;
//            }
//
//            // Draw group (visible elements)
//            if(event.getGroup() != null){
//                CollapseGroup c = event.getGroup();
//                c.draw(g2d, lineHeight, yOffset);
//            }
        }
    }

    // TODO: Recursive doesn't work! Count line numbers better!
    private int getCollapsedGroupLinesCount(Event event, int found){
//        if(event.getGroup() != null){
//            found += event.getGroup().getItemsCount();
//            for(Event x : event.getGroup().getEvents()){
//                getCollapsedGroupLinesCount(x, found);
//            }
//        }
        return found;
    }

    public MainView getView() {
        return view;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
        repaint();
    }
}
