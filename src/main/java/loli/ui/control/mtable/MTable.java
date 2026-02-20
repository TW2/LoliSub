package loli.ui.control.mtable;

import loli.Exchange;
import loli.enumeration.AssEventType;
import loli.enumeration.DrawColor;
import loli.enumeration.ISO_3166;
import loli.helper.AssTime;
import loli.helper.OnLoad;
import loli.subtitle.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class MTable extends JPanel {
    private final Exchange exchange;

    public enum Stripped {
        Off, Partially, On;
    }

    private Stripped stripped = Stripped.Partially;

    private final List<Voyager> voyagers;
    private final JScrollBar scrollBar;
    private int vBarOffset;

    public MTable(Exchange exchange) {
        this.exchange = exchange;
        setDoubleBuffered(true);
        voyagers = new ArrayList<>();
        scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        scrollBar.setMinimum(0);
        scrollBar.setMaximum(10000);
        vBarOffset = 0;

        setComponentPopupMenu(createPopupMenu());

        setLayout( new BorderLayout());
        add(scrollBar, BorderLayout.EAST);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getButton() == MouseEvent.BUTTON1) {
                    int row = 0, y = vBarOffset + 20;
                    for(Voyager v : voyagers) {
                        y += v.isCollapsed() ? 20 : 40 + v.getVoyagers().size() * 20;
                        if(y < e.getY()) {
                            row += 1;
                        }else{
                            break;
                        }
                    }
                    if(e.getY() > 20 && row < voyagers.size()) {
                        switch(e.getClickCount()){
                            case 1 -> {
                                if(e.getX() < 20){
                                    boolean value = voyagers.get(row).isCollapsed();
                                    voyagers.get(row).setCollapsed(!value);
                                }else{
                                    for(Voyager v : voyagers) {
                                        v.setSelected(false);
                                    }
                                    voyagers.get(row).setSelected(true);
                                }
                                repaint();
                            }
                            case 2 -> {

                            }
                        }
                    }
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int hMax = getVoyagerTotalHeight();
                int thickness = 20;
                if(e.getWheelRotation() < 0) {
                    vBarOffset = Math.min(vBarOffset + thickness, 0);
                }else if(e.getWheelRotation() > 0) {
                    vBarOffset = Math.max(vBarOffset - thickness, -(hMax - getHeight() + thickness * 2));
                }
                if(vBarOffset != scrollBar.getValue() && !voyagers.isEmpty()){
                    scrollBar.setValue(-(vBarOffset / thickness));
                }
                repaint();
            }
        });

        scrollBar.addAdjustmentListener(e -> {
            int thickness = 20;
            vBarOffset = -(e.getValue() * thickness);
            if(vBarOffset != scrollBar.getValue() && !voyagers.isEmpty()){
                scrollBar.setValue(-(vBarOffset / thickness));
            }
            repaint();
        });
    }

    public List<Voyager> getVoyagers() {
        return voyagers;
    }

    public void addLine(Voyager voyager){
        voyagers.add(voyager);
        updateVerticalScrollBar();
        repaint();
    }

    public void addLine(ISO_3166 language, boolean visible, Event event){
        addLine(new Voyager(language, visible, event));
    }

    private int getVoyagerTotalHeight(){
        int totalHeight = 0;
        for(Voyager voyager : voyagers) {
            if(voyager != null) {
                totalHeight += voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
            }
        }
        return totalHeight;
    }

    public void updateVerticalScrollBar(){
        scrollBar.setMinimum(0);
        scrollBar.setMaximum(Math.max(0, getVoyagerTotalHeight() / 20));
        //System.out.printf("r(%d) -(%d) +(%d)\n", rows.size(), scrollBar.getMinimum(), scrollBar.getMaximum());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //------------------------------------------------------------
        int offset = vBarOffset > 0 ? -vBarOffset : vBarOffset;

        // Reset
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Voyagers
        int y = 20 + offset;
        int counter = 0;

        // The background
        for (Voyager voyager : voyagers) {
            if(voyager != null) {
                int h = voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
                if(h >= vBarOffset && vBarOffset < h + getHeight()) {
                    g2d.setColor(
                            voyager.isVisible() ?
                                    voyager.isSelected() ?
                                            DrawColor.blue.getColor(.2f) : Color.white
                                    : DrawColor.light_gray.getColor(.2f)
                    );
                    g2d.fillRect(0, y, getWidth(), h);
                }
                y += h;
            }
        }

        y = 20 + offset;

        // The lines
        for (Voyager voyager : voyagers) {
            if(voyager != null) {
                int h = voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
                if(h >= vBarOffset && vBarOffset < h + getHeight()) {
                    g2d.setColor(DrawColor.black.getColor(.5f));
                    g2d.drawLine(0, y + h, getWidth(), y + h);
                }
                y += h;
            }
        }

        y = 20 + offset;

        // The data
        for (Voyager voyager : voyagers) {
            if(voyager != null) {
                counter++;
                int h = voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
                if(h >= vBarOffset && vBarOffset < h + getHeight()) {
                    g2d.setColor(Color.black);
                    // +/-
                    Ellipse2D plusMinus = new Ellipse2D.Double(2, y + 2, 16, 16);
                    if(voyager.isCollapsed()) {
                        // Plus
                        if(!voyager.getVoyagers().isEmpty()) {
                            g2d.draw(plusMinus);
                            g2d.drawLine(6, y + h - 10, 14, y + h - 10);
                            g2d.drawLine(10, y + h - 14, 10, y + h - 6);
                        }
                    }else{
                        // Minus
                        g2d.draw(plusMinus);
                        g2d.drawLine(6, y + 10, 14, y + 10);
                    }
                    // Line number
                    g2d.drawString(String.valueOf(counter), 27, y + 16);
                    // Layer
                    g2d.drawString(String.format("%d", voyager.getEvent().getLayer()), 155, y + 16);
                    // Flag
                    try{
                        int wi, xi;
                        if(voyager.isCollapsed()) {
                            // Plus
                            ImageIcon i1 = OnLoad.locations(voyager.getLanguage());
                            wi = 18 * i1.getIconWidth() / i1.getIconHeight();
                            xi = (39 - wi) / 2;
                            g2d.drawImage(i1.getImage(), 205 + xi, y+1, wi, 18,null);
                        }else{
                            // Minus
                            ImageIcon i1 = OnLoad.locations(voyager.getLanguage());
                            wi = 18 * i1.getIconWidth() / i1.getIconHeight();
                            xi = (39 - wi) / 2;
                            g2d.drawImage(i1.getImage(), 205 + xi, y+1, wi, 18,null);
                            int ya = y + 40;
                            for(Voyager v : voyager.getVoyagers()) {
                                i1 = OnLoad.locations(v.getLanguage());
                                wi = 18 * i1.getIconWidth() / i1.getIconHeight();
                                xi = (39 - wi) / 2;
                                g2d.drawImage(i1.getImage(), 205 + xi, ya+1, wi, 18,null);
                                ya += 20;
                            }
                        }
                    }catch(Exception _){
                        // Wrong locale
                    }
                    // Start time in 0:00:00.00 format
                    g2d.drawString(voyager.getEvent().getStart().toAss(), 247, y + 16);
                    // End time in 0:00:00.00 format
                    g2d.drawString(voyager.getEvent().getEnd().toAss(), 327, y + 16);
                    // Style
                    g2d.drawString(voyager.getEvent().getStyle().getName(), 407, y + 16);
                    // Actor
                    g2d.drawString(voyager.getEvent().getName().getName(), 487, y + 16);
                    // Text
                    if(voyager.isCollapsed()) {
                        // Plus
                        g2d.drawString(applyStrip(voyager.getEvent().getText()), 567, y + 16);
                    }else{
                        // Minus
                        g2d.drawString(applyStrip(voyager.getEvent().getText()), 567, y + 16);
                        int ya = y + 40;
                        for(Voyager v : voyager.getVoyagers()) {
                            g2d.drawString(applyStrip(v.getEvent().getText()), 567, ya + 16);
                            ya += 20;
                        }
                    }

                    g2d.setColor(Color.gray);
                    if(!voyager.isCollapsed()) {
                        // Duration in 0:00:00.00 format
                        g2d.drawString("Duration :", 248, y + 20 + 16);
                        // Duration
                        AssTime s = voyager.getEvent().getStart();
                        AssTime e = voyager.getEvent().getEnd();
                        String duration = AssTime.getAssDuration(s, e);
                        g2d.drawString(duration, 327, y + 20 + 16);
                        // Effect
                        g2d.drawString("Effect :", 407, y + 20 + 16);
                        g2d.drawString(voyager.getEvent().getEffect().getName(), 487, y + 20 + 16);
                        // CPS
                        g2d.drawString(String.format("%.02f cps",
                                voyager.getEvent().getCPS()
                        ), 567, y + 20 + 16);
                        // CPL
                        g2d.drawString(String.format("%d cpl",
                                Math.round(voyager.getEvent().getCPL())
                        ), 647, y + 20 + 16);
                        // ML, MR, MV
                        g2d.drawString(String.format("Margins : %d %d %d",
                                voyager.getEvent().getMarginL(),
                                voyager.getEvent().getMarginR(),
                                voyager.getEvent().getMarginV()
                        ), 727, y + 20 + 16);
                    }
                }
                y += h;
            }
        }

        // The header
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), 20);
        g2d.setColor(Color.gray);
        g2d.drawRect(0, 0, getWidth()-1, 20-1);
        // Collapse(-) or not(+)
        g2d.drawString("+/-", 4, 20 - 4);
        g2d.drawLine(25, 0, 25, 20-1);
        // Line
        g2d.drawString("Line", 37, 20 - 4);
        g2d.drawLine(75, 0, 75, 20-1);
        // Bookmark
        g2d.drawString("Bookmark", 82, 20 - 4);
        g2d.drawLine(153, 0, 153, 20-1);
        // Layer
        g2d.drawString("Layer", 162, 20 - 4);
        g2d.drawLine(205, 0, 205, 20-1);
        // Flag
        g2d.drawString("Flag", 211, 20 - 4);
        g2d.drawLine(244, 0, 244, 20-1);
        // Start
        g2d.drawString("Start", 269, 20 - 4);
        g2d.drawLine(324, 0, 324, 20-1);
        // End
        g2d.drawString("End", 352, 20 - 4);
        g2d.drawLine(404, 0, 404, 20-1);
        // Style
        g2d.drawString("Style", 427, 20 - 4);
        g2d.drawLine(484, 0, 484, 20-1);
        // Actor
        g2d.drawString("Actor", 507, 20 - 4);
        g2d.drawLine(564, 0, 564, 20-1);
        // Text
        g2d.drawString("Text", 760, 20 - 4);

        //------------------------------------------------------------
        g2d.dispose();
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        // Cut, Copy, Paste, Delete, Duplicate
        JMenuItem mCut = new JMenuItem("Cut");
        JMenuItem mCopy = new JMenuItem("Copy");
        JMenuItem mPaste = new JMenuItem("Paste");
        JMenuItem mDelete = new JMenuItem("Delete");
        JMenuItem mDuplicate = new JMenuItem("Duplicate");

        popupMenu.add(mCut);
        popupMenu.add(mCopy);
        popupMenu.add(mPaste);
        popupMenu.add(mDelete);
        popupMenu.add(mDuplicate);


        return popupMenu;
    }

    private String applyStrip(String s){
        String str = "";
        switch(stripped){
            case On -> {
                if(s.contains("{\\")){
                    try {
                        str = s.replaceAll("\\{[^}]+}", "");
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
                        String partiallyStrippedSymbol = "◆";
                        str = s.replaceAll("\\{[^}]+}", partiallyStrippedSymbol);
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

    public void addEvent(Event event, ISO_3166 f1, ISO_3166 f2) {
        if(f1.equals(f2)){
            voyagers.add(new Voyager(f1, event.getType() != AssEventType.Comment, event));
        }else{
            int index = -1;
            for(int i=0;i<voyagers.size();i++){
                Voyager voyager = voyagers.get(i);
                if(voyager.isSelected()){
                    index = i;
                    break;
                }
            }
            if(index != -1 && !voyagers.isEmpty()){
                voyagers.get(index).getVoyagers().add(new Voyager(f2, event.getType() != AssEventType.Comment, event));
            }
        }
        updateVerticalScrollBar();
        repaint();
    }

    public void replaceEvent(Event event, ISO_3166 f1, ISO_3166 f2) {
        int index = -1;
        for(int i=0;i<voyagers.size();i++){
            Voyager voyager = voyagers.get(i);
            if(voyager.isSelected()){
                index = i;
                break;
            }
        }
        if(index != -1){
            if(f1.equals(f2)){
                List<Voyager> doNotRemoveChildren = voyagers.get(index).getVoyagers();
                boolean isCollapsed = voyagers.get(index).isCollapsed();
                boolean isSelected = voyagers.get(index).isSelected();
                voyagers.remove(index);
                Voyager voyager = new Voyager(f1, event.getType() != AssEventType.Comment, event);
                voyager.setCollapsed(isCollapsed);
                voyager.setSelected(isSelected);
                voyager.getVoyagers().addAll(doNotRemoveChildren);
                voyagers.add(index, voyager);
            }else{
                int index2 = -1;
                for (Voyager v : voyagers) {
                    for (int j = 0; j < v.getVoyagers().size(); j++) {
                        ISO_3166 iso = v.getVoyagers().get(j).getLanguage();
                        if (iso.equals(f2)) {
                            index2 = j;
                            break;
                        }
                    }
                    if (index2 != -1) {
                        v.getVoyagers().remove(index2);
                        v.getVoyagers().add(index2, new Voyager(f2, event.getType() != AssEventType.Comment, event));
                        break;
                    }
                }
            }
            updateVerticalScrollBar();
            repaint();
        }
    }

    public void beforeEvent(Event event, ISO_3166 f1, ISO_3166 f2) {
        int index = -1;
        for(int i=0;i<voyagers.size();i++){
            Voyager voyager = voyagers.get(i);
            if(voyager.isSelected()){
                index = i;
                break;
            }
        }
        if(index != -1){
            if(f1.equals(f2)){
                voyagers.add(index, new Voyager(f1, event.getType() != AssEventType.Comment, event));
            }
            updateVerticalScrollBar();
            repaint();
        }
    }

    public void afterEvent(Event event, ISO_3166 f1, ISO_3166 f2) {
        try{
            if(f1.equals(f2)){
                int index = -1;
                for(int i=0;i<voyagers.size();i++){
                    Voyager voyager = voyagers.get(i);
                    if(voyager.isSelected()){
                        index = i;
                        break;
                    }
                }
                if(index != -1){
                    voyagers.add(index+1, new Voyager(f1, event.getType() != AssEventType.Comment, event));
                    updateVerticalScrollBar();
                    repaint();
                }
            }
        }catch(Exception _){
            addEvent(event, f1, f2);
        }
    }

    public void setStripped(Stripped stripped) {
        this.stripped = stripped;
        repaint();
    }
}
