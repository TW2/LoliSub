package loli.ui.control.table;

import loli.enumeration.AssEventType;
import loli.enumeration.DrawColor;
import loli.helper.AssActor;
import loli.helper.AssEffect;
import loli.helper.AssStyle;
import loli.helper.AssTime;
import loli.subtitle.Event;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MainRenderer extends JPanel implements TableCellRenderer {

    public enum Stripped {
        Off, Partially, On;
    }

    private Stripped stripped = Stripped.Partially;
    private String partiallyStrippedSymbol = "◆";


    private final JLabel label; // Label for content
    private final JLabel labelEntering; // Label for collapsedGroup
    private final JLabel labelQuiting; // Label for collapsedGroup
    private Color collapsedGroupEnteringColor;
    private Color collapsedGroupQuitingColor;

    public MainRenderer() {
        setOpaque(true);
        label = new JLabel();
        label.setOpaque(true);
        labelEntering = new JLabel();
        labelEntering.setOpaque(true);
        labelQuiting = new JLabel();
        labelQuiting.setOpaque(true);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(labelEntering, BorderLayout.SOUTH);
        add(labelQuiting, BorderLayout.NORTH);
        collapsedGroupEnteringColor = DrawColor.green_yellow.getColor();
        collapsedGroupQuitingColor = DrawColor.green_yellow.getColor();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column) {

        Event event = null;
        Object obj = table.getModel().getValueAt(row, 12);
        if(obj instanceof Event ev){
            event = ev;
        }

        Color bg;

        if(event != null && event.getType() == AssEventType.Comment){
            bg = DrawColor.violet.getColor();
        }else{
            // Get table background (avoid searching from any other way)
            // FlatLaf properties >> Table.background
            bg = UIManager.getColor("Table.background");
        }

        // Get table foreground (avoid searching from any other way)
        // FlatLaf properties >> Table.foreground
        Color fg = UIManager.getColor("Table.foreground");

        switch(value){
            case AssEventType x -> { label.setText(x == AssEventType.Comment ? "#" : "D"); }
            case AssTime x -> { label.setText(x.toString()); }
            case AssStyle x -> { label.setText(x.toString()); }
            case AssActor x -> { label.setText(x.toString()); }
            case AssEffect x -> { label.setText(x.toString()); }
            case Event x -> { label.setText(applyStrip(x.getText())); }
            case Integer x -> { label.setText(Integer.toString(x)); }
            case String x -> { label.setText(applyStrip(x)); }
            default -> {}
        }

        if(isSelected){
            bg = UIManager.getColor("Table.selectionBackground");
            fg = UIManager.getColor("Table.selectionForeground");
        }

        // Set color to label
        label.setBackground(bg);
        label.setForeground(fg);

        // ----------------------------
        // Collapse cases
        // ----------------------------
//        if(event != null){
//            labelEntering.setPreferredSize(new Dimension(getWidth(),0));
//            labelQuiting.setPreferredSize(new Dimension(getWidth(),0));
//            if(event.isCollapseElementFollow()){
//                // Draw a line on bottom
//                labelEntering.setBackground(collapsedGroupEnteringColor);
//                labelEntering.setPreferredSize(new Dimension(getWidth(),1));
//            }
//            if(event.isCollapseElementPrevious()){
//                // Draw a line on top
//                labelQuiting.setBackground(collapsedGroupQuitingColor);
//                labelQuiting.setPreferredSize(new Dimension(getWidth(),1));
//            }
//        }

        return this;
    }

    private String applyStrip(String s){
        String str = "";
        switch(stripped){
            case On -> {
                if(s.contains("{\\")){
                    try {
                        str = s.replaceAll("\\{[^\\}]+\\}", "");
                    } catch (Exception e) {
                        str = s;
                    }
                }else{
                    str = s;
                }
            }
            case Partially -> {
                if(s.contains("{\\")){
                    try {
                        str = s.replaceAll("\\{[^\\}]+\\}", partiallyStrippedSymbol);
                    } catch (Exception e) {
                        str = s;
                    }
                }else{
                    str = s;
                }
            }
            case Off -> {
                str = s;
            }
        }
        return str;
    }

    public void setStripped(Stripped stripped) {
        this.stripped = stripped;
    }

    public void setPartiallyStrippedSymbol(String partiallyStrippedSymbol) {
        this.partiallyStrippedSymbol = partiallyStrippedSymbol;
    }

    public void setCollapsedGroupEnteringColor(Color collapsedGroupEnteringColor) {
        this.collapsedGroupEnteringColor = collapsedGroupEnteringColor;
    }

    public void setCollapsedGroupQuitingColor(Color collapsedGroupQuitingColor) {
        this.collapsedGroupQuitingColor = collapsedGroupQuitingColor;
    }
}
