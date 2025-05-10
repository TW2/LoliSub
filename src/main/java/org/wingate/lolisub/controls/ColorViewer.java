package org.wingate.lolisub.controls;

import javax.swing.*;
import java.awt.*;

public class ColorViewer extends JPanel {

    private Color color;

    private final JLabel lblText;
    private final JTextField tfColor;
    private final ColorPanel panColor;
    private final JSlider slideAlpha;

    public ColorViewer(String text, Color c) {
        color = c;
        lblText = new JLabel(text);
        panColor = new ColorPanel();
        tfColor = new JTextField(getBGR(panColor.getColor(), true));
        DefaultBoundedRangeModel slideModel = new DefaultBoundedRangeModel();
        slideModel.setMinimum(0);
        slideModel.setMaximum(255);
        slideAlpha = new JSlider(slideModel);
        slideAlpha.addChangeListener((_)->{
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                    255 - slideAlpha.getValue());
            tfColor.setText(getBGR(color, true));
            panColor.setColor(color);
            panColor.repaint();
        });

        setLayout(new GridLayout(1, 4, 4, 0));
        add(lblText);
        add(tfColor);
        add(panColor);
        add(slideAlpha);
    }

    public String getBGR(Color c, boolean alpha){
        StringBuilder sb = new StringBuilder();
        String a = Integer.toHexString(255 - c.getAlpha());
        if(a.length() < 2) a = "0" + a;
        String r = Integer.toHexString(c.getRed());
        if(r.length() < 2) r = "0" + r;
        String g = Integer.toHexString(c.getGreen());
        if(g.length() < 2) g = "0" + g;
        String b = Integer.toHexString(c.getBlue());
        if(b.length() < 2) b = "0" + b;

        return (alpha ? a+b+g+r : b+g+r).toUpperCase();
    }

    public static class ColorPanel extends JPanel {

        private Color color;

        public ColorPanel(Color color){
            this.color = color;
        }

        public ColorPanel(){
            this(Color.blue);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(new Color(color.getRed(), color.getGreen(), color.getGreen()));
            g.fillRect(0,0,getWidth()/2, getHeight());

            g.setColor(color);
            g.fillRect(getWidth()/2, 0, getWidth()/2, getHeight());
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        tfColor.setText(getBGR(color, true));
        panColor.setColor(color);
        panColor.repaint();
    }

    public JLabel getLblText() {
        return lblText;
    }

    public JTextField getTfColor() {
        return tfColor;
    }

    public ColorPanel getPanColor() {
        return panColor;
    }

    public JSlider getSlideAlpha() {
        return slideAlpha;
    }
}
