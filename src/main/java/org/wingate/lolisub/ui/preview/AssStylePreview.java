package org.wingate.lolisub.ui.preview;

import javax.swing.*;
import java.awt.*;

public class AssStylePreview extends JPanel {

    private static final int UNIT = 10;
    private String sentenceSample;

    public AssStylePreview() {
        sentenceSample = "A beautiful world is a green one.";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.lightGray);
        boolean shift = true;
        for(int y=0; y<getHeight(); y+=UNIT){
            for(int x=0; x<getWidth(); x+=UNIT*2){
                g.fillRect(shift ? x + UNIT : x, y, UNIT, UNIT);
            }
            shift = !shift;
        }
    }

    public String getSentenceSample() {
        return sentenceSample;
    }

    public void setSentenceSample(String sentenceSample) {
        this.sentenceSample = sentenceSample;
    }
}
