package org.wingate.lolisub.ui.audiovideo;

import org.wingate.lolisub.LoliSub;
import org.wingate.lolisub.ass.AssTime;
import org.wingate.lolisub.helper.DrawColor;
import org.wingate.lolisub.ui.table.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @deprecated Use WaveFormPanel and SpectrogramPanel instead
 * @see WaveFormPanel
 */
public class AudioWaveForm extends JPanel {

    private final MainView view;

    private long currentMicros;
    private long startMicros;
    private long endMicros;

    private long offsetMicros;
    private int xOffset;
    private String mediaPath;

    private Color areaStartColor;
    private Color areaEndColor;
    private Color areaOverlay;
    private Color mouseColor;
    private Color keyFramesColor;
    private Color secondsMarkColor;
    private Color minutesMarkColor;
    private Color hoursMarkColor;

    private BufferedImage imgCurrent;
    private BufferedImage imgNext;

    private Point mouseMovePoint;
    private Point mouseStartPoint;
    private Point mouseEndPoint;

    public AudioWaveForm(boolean isDoubleBuffered, MainView view) {
        super(isDoubleBuffered);
        this.view = view;
        imgCurrent = null;

        mouseMovePoint = null;
        mouseStartPoint = null;
        mouseEndPoint = null;

        currentMicros = 0L;
        startMicros = 0L;
        endMicros = 0L;

        xOffset = 0;

        areaStartColor = Color.green;
        areaEndColor = Color.yellow;
        areaOverlay = DrawColor.black.getColor(.3f);
        mouseColor = Color.white;
        keyFramesColor = DrawColor.cyan.getColor(.7f);
        secondsMarkColor = Color.pink;
        minutesMarkColor = Color.magenta;
        hoursMarkColor = DrawColor.alice_blue.getColor();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                mouseMovePoint = e.getPoint();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getButton() == MouseEvent.BUTTON1){
                    // Left button
                    mouseStartPoint = e.getPoint();
                }

                if(e.getButton() == MouseEvent.BUTTON3){
                    // Right button
                    mouseEndPoint = e.getPoint();
                }

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(DrawColor.dark_slate_blue.getColor());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if(imgCurrent != null){
            g2d.drawImage(imgCurrent, -xOffset, 0, null);
            g2d.drawImage(imgNext, -xOffset + getWidth(), 0, null);

            if(mouseMovePoint != null){
                g2d.setColor(mouseColor);
                g2d.drawLine(mouseMovePoint.x, 0, mouseMovePoint.x, getHeight());
                String content = String.format("%s (%s)",
                        getTimePixels(mouseMovePoint.x),
                        LoliSub.RSX.getString("waveCursor"));
                drawAssTime(g2d, content, -2, 0, -mouseMovePoint.x);
            }

            if(mouseStartPoint != null){
                g2d.setColor(areaStartColor);
                g2d.drawLine(mouseStartPoint.x, 0, mouseStartPoint.x, getHeight());
                String content = String.format("%s (%s)",
                        getTimePixels(mouseStartPoint.x),
                        LoliSub.RSX.getString("waveAreaStart"));
                drawAssTime(g2d, content, -2, 0, -mouseStartPoint.x);
                view.getEditor().setToLockStart(getTime(mouseStartPoint.x));
            }

            if(mouseEndPoint != null){
                g2d.setColor(areaEndColor);
                g2d.drawLine(mouseEndPoint.x, 0, mouseEndPoint.x, getHeight());
                String content = String.format("%s (%s)",
                        getTimePixels(mouseEndPoint.x),
                        LoliSub.RSX.getString("waveAreaEnd"));
                drawAssTime(g2d, content, -2, 0, -mouseEndPoint.x);
                view.getEditor().setToLockEnd(getTime(mouseEndPoint.x));
            }

            if(mouseStartPoint != null && mouseEndPoint != null
                    && mouseStartPoint != mouseEndPoint){
                g2d.setColor(areaOverlay);
                g2d.fillRect(
                        mouseStartPoint.x, 0,
                        mouseEndPoint.x - mouseStartPoint.x, getHeight()
                );
            }
        }
    }

    public AssTime getTime(int x){
        long period = endMicros - startMicros;
        // period <> max
        // m <> x
        long m = period * x / Math.max(1L, getWidth());
        return new AssTime((double) (m + startMicros) / 1_000d); // ms
    }

    public String getTimePixels(int x) {
        return getTime(x).toAss();
    }

    public void drawAssTime(Graphics2D g2d, String content, int rx, int ry, int x){
        // Rotation with anchor
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(90), rx, ry);
        g2d.setTransform(transform);
        g2d.drawString(content, 5, x + 10);
        transform.rotate(Math.toRadians(-90), rx, ry);
        g2d.setTransform(transform);
    }

    //----------------------------------------------------------------

    public void setCurrentMicros(long currentMicros) {
        this.currentMicros = currentMicros;
    }

    public void setStartMicros(long startMicros) {
        this.startMicros = startMicros;
    }

    public void setEndMicros(long endMicros) {
        this.endMicros = endMicros;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setAreaStartColor(Color areaStartColor) {
        this.areaStartColor = areaStartColor;
    }

    public void setAreaEndColor(Color areaEndColor) {
        this.areaEndColor = areaEndColor;
    }

    public void setAreaOverlay(Color areaOverlay) {
        this.areaOverlay = areaOverlay;
    }

    public void setMouseColor(Color mouseColor) {
        this.mouseColor = mouseColor;
    }

    public void setKeyFramesColor(Color keyFramesColor) {
        this.keyFramesColor = keyFramesColor;
    }

    public void setSecondsMarkColor(Color secondsMarkColor) {
        this.secondsMarkColor = secondsMarkColor;
    }

    public void setMinutesMarkColor(Color minutesMarkColor) {
        this.minutesMarkColor = minutesMarkColor;
    }

    public void setHoursMarkColor(Color hoursMarkColor) {
        this.hoursMarkColor = hoursMarkColor;
    }

    public void setOffsetMicros(long offset) {
        offsetMicros = offset;
        // period <> width
        // offset <> x
        xOffset = Math.toIntExact(offset * getWidth() / Math.max(1L, endMicros - startMicros));
        repaint();
    }
}
