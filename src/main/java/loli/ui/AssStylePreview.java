package loli.ui;

import loli.enumeration.AssEventType;
import loli.helper.AssStyle;
import loli.helper.AssTime;
import loli.io.ASS;
import loli.subtitle.Event;
import loli.rendering.AGraphicElement;
import loli.rendering.Char;
import loli.rendering.Converter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AssStylePreview extends JPanel {

    private static final int UNIT = 10;
    private String sentenceSample;
    private ASS ass;

    public AssStylePreview() {
        setDoubleBuffered(true);
        ass = new ASS();
        sentenceSample = "A beautiful world is a green one.";

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setResolution(
                        e.getComponent().getWidth(),
                        e.getComponent().getHeight()
                );
            }
        });
    }

    private void setResolution(int w, int h){
        ass.getInfos().setPlayResX(w);
        ass.getInfos().setPlayResY(h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
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

        if(ass.getEvents().isEmpty()) return;

        // Foreground
        BufferedImage image = doImage(
                ass.getInfos().getPlayResX(),
                ass.getInfos().getPlayResY()
        );

        g.drawImage(image, 0, 0, null);
    }

    public String getSentenceSample() {
        return sentenceSample;
    }

    public void setSentenceSample(AssStyle style, String sentenceSample) {
        this.sentenceSample = sentenceSample;
        if(!sentenceSample.isEmpty() && style != null){
            ass = assOf(style, sentenceSample);
            repaint();
        }
    }

    private BufferedImage doImage(int width, int height){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );

        Event event = ass.getEvents().getFirst();

        Converter converter = Converter.creteShapes(event, ass);
        // Calculation
        double sentenceWidth = 0d;
        double sentenceHeight = 0d;
        for(AGraphicElement element : converter.getElements()){
            if(element instanceof Char c){
                sentenceWidth += c.getAdvance();
                sentenceWidth += c.getExtraSpacing();
                sentenceHeight = Math.max(sentenceHeight, c.getHeight());
            }
        }

        // Position
        AffineTransform tr = new AffineTransform();
        tr.translate(
                (getWidth() - sentenceWidth) / 2d, // Center X
                ((getHeight() - sentenceHeight) / 2d) + sentenceHeight // Center Y
        );
        g.setTransform(tr);

        // Draw
        for(AGraphicElement element : converter.getElements()){
            if(element instanceof Char c){
                // Shadow
                // Outline
                g.setColor(event.getStyle().getOutlineColor().getColor());
                c.draw(g);
                // Text
                g.setColor(event.getStyle().getTextColor().getColor());
                c.fill(g);
                tr.translate(c.getAdvance() + c.getExtraSpacing(), 0d);
                g.setTransform(tr);
            }
        }

        g.dispose();
        return image;
    }

    private static ASS assOf(AssStyle style, String sentence){
        ASS ass = new ASS();

        Event event = new Event();
        event.setStart(new AssTime(0L));
        event.setEnd(new AssTime(1000L));
        event.setStyle(style);
        event.setType(AssEventType.Dialogue);
        event.setText(sentence);

        ass.getStyles().add(style);
        ass.getEvents().add(event);

        return ass;
    }

    public static void main(String[] args) {
        ASS ass = assOf(new AssStyle(), "{\\fs72}A beautiful world is a green one.");

        BufferedImage image = new BufferedImage(
                1280, 720, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(Color.white);
        g.fillRect(0, 0, 1280, 720);

        g.setColor(Color.lightGray);
        boolean shift = true;
        for(int y=0; y<720; y+=UNIT){
            for(int x=0; x<1280; x+=UNIT*2){
                g.fillRect(shift ? x + UNIT : x, y, UNIT, UNIT);
            }
            shift = !shift;
        }

        AffineTransform tr = new AffineTransform();
        tr.translate(20, 60);
        g.setTransform(tr);

        Converter converter = Converter.creteShapes(ass.getEvents().getFirst(), ass);
        for(AGraphicElement element : converter.getElements()){
            if(element instanceof Char c){
                g.setColor(Color.blue);
                c.draw(g);
                g.setColor(Color.red);
                c.fill(g);
                tr.translate(c.getAdvance() + c.getExtraSpacing(), 0d);
                g.setTransform(tr);
            }
        }

        try {
            ImageIO.write(
                    image,
                    "png",
                    new File("C:\\Users\\util2\\Desktop\\testass\\ass.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g.dispose();
    }
}
