package loli.ui.control.audiovideo;

import loli.enumeration.DrawColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class VideoTimeBar extends JPanel {

    private final VideoPlayer player;

    private Point mouseFrameLocation;

    private List<Long> keyFrames;
    private long mediaLengthMicros;
    private double fps;
    private int frame;

    private long currentMicros;
    private long startMicros;
    private long endMicros;

    private Color themeBackgroundColor;
    private Color themeForegroundColor;
    private Color timeBorderColor;
    private Color timeInnerRemainingColor;
    private Color timeInnerElapsedColor;
    private Color followBorderColor;
    private Color followBackgroundColor;
    private Color followForegroundColor;
    private Color followColor;
    private Color keyFrameColor;
    private Color mouseColor;
    private Color startMarkColor;
    private Color endMarkColor;
    private Color markOverlayColor;

    public VideoTimeBar(boolean isDoubleBuffered, VideoPlayer player) {
        super(isDoubleBuffered);
        this.player = player;

        mouseFrameLocation = new Point();

        keyFrames = new ArrayList<>();
        mediaLengthMicros = -1L;
        fps = 24000d / 1001d;
        frame = 0;

        currentMicros = -1L;
        startMicros = -1L;
        endMicros = -1L;

        themeBackgroundColor = UIManager.getColor("Panel.background");
        themeForegroundColor = UIManager.getColor("Panel.foreground");
        timeBorderColor = UIManager.getColor("Panel.foreground");
        timeInnerRemainingColor = DrawColor.corn_flower_blue.getColor();
        timeInnerElapsedColor = DrawColor.black.getColor(0.5f);
        followBorderColor = UIManager.getColor("Panel.foreground");
        followBackgroundColor = UIManager.getColor("Panel.background");
        followForegroundColor = UIManager.getColor("Panel.foreground");
        followColor = UIManager.getColor("Panel.foreground");
        keyFrameColor = DrawColor.white.getColor();
        mouseColor = DrawColor.red.getColor();
        startMarkColor = Color.yellow;
        endMarkColor = Color.magenta;
        markOverlayColor = DrawColor.dark_violet.getColor();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouseFrameLocation = e.getPoint();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(themeBackgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(themeForegroundColor);
        g.fillRect(6, (getHeight() / 2) - 2, getWidth() - 12, 4);

        g.setColor(Color.white);
        g.fillRect(7, (getHeight() / 2) - 2, getWidth() - 14, 4);

        g.setColor(timeInnerRemainingColor);
        g.fillRect(7, (getHeight() / 2) - 2, getWidth() - 14, 4);

        if(mediaLengthMicros > -1L && currentMicros > -1L){
            g.setColor(timeInnerElapsedColor);
            int pW = (int)((getWidth() - 14) * currentMicros / mediaLengthMicros);
            g.fillRect(7, (getHeight() / 2) - 2, pW, 4);

            g.setColor(keyFrameColor);
            for(long micros : keyFrames){
                if(micros == 0L || micros == mediaLengthMicros) continue;
                int pX = (int)((getWidth() - 14) * micros / mediaLengthMicros);
                g.drawLine(7 + pX, (getHeight() / 2) - 2, 7 + pX, (getHeight() / 2) + 2);
            }

            g.setColor(followColor);
            g.fillRect(7 + pW - 2, (getHeight() / 2) - 1, 4, 2);

            g.drawString(
                    getTime(currentMicros),
                    7 + pW - 20,
                    12
            );

            g.drawString(
                    Integer.toString(frame),
                    7 + pW - 20,
                    30
            );

            g.setColor(Color.cyan.brighter());
            g.fillRect(mouseFrameLocation.x, (getHeight() / 2) - 1, 4, 2);
        }
    }

    private String getTime(long micros){
        long msTime = micros / 1000L;
        int hh = (int)(msTime / 3600000d);
        int mm = (int)((msTime - 3600000d * hh) / 60000d);
        int ss = (int)((msTime - 3600000d * hh - 60000d * mm) / 1000d);
        int cs = (int)((msTime - 3600000d * hh - 60000d * mm - 1000d * ss) / 10d);

        return String.format("%s:%s:%s.%s",
                hh,
                mm < 10 ? "0" + mm : mm,
                ss < 10 ? "0" + ss : ss,
                cs < 10 ? "0" + cs : cs
        );
    }

    public long getCurrentMicros() {
        return currentMicros;
    }

    public void setCurrentMicros(long currentMicros) {
        this.currentMicros = currentMicros;
    }

    public long getStartMicros() {
        return startMicros;
    }

    public void setStartMicros(long startMicros) {
        this.startMicros = startMicros;
    }

    public long getEndMicros() {
        return endMicros;
    }

    public void setEndMicros(long endMicros) {
        this.endMicros = endMicros;
    }

    public Color getThemeBackgroundColor() {
        return themeBackgroundColor;
    }

    public void setThemeBackgroundColor(Color themeBackgroundColor) {
        this.themeBackgroundColor = themeBackgroundColor;
    }

    public Color getThemeForegroundColor() {
        return themeForegroundColor;
    }

    public void setThemeForegroundColor(Color themeForegroundColor) {
        this.themeForegroundColor = themeForegroundColor;
    }

    public Color getTimeBorderColor() {
        return timeBorderColor;
    }

    public void setTimeBorderColor(Color timeBorderColor) {
        this.timeBorderColor = timeBorderColor;
    }

    public Color getTimeInnerRemainingColor() {
        return timeInnerRemainingColor;
    }

    public void setTimeInnerRemainingColor(Color timeInnerRemainingColor) {
        this.timeInnerRemainingColor = timeInnerRemainingColor;
    }

    public Color getTimeInnerElapsedColor() {
        return timeInnerElapsedColor;
    }

    public void setTimeInnerElapsedColor(Color timeInnerElapsedColor) {
        this.timeInnerElapsedColor = timeInnerElapsedColor;
    }

    public Color getFollowBorderColor() {
        return followBorderColor;
    }

    public void setFollowBorderColor(Color followBorderColor) {
        this.followBorderColor = followBorderColor;
    }

    public Color getFollowBackgroundColor() {
        return followBackgroundColor;
    }

    public void setFollowBackgroundColor(Color followBackgroundColor) {
        this.followBackgroundColor = followBackgroundColor;
    }

    public Color getFollowForegroundColor() {
        return followForegroundColor;
    }

    public void setFollowForegroundColor(Color followForegroundColor) {
        this.followForegroundColor = followForegroundColor;
    }

    public Color getFollowColor() {
        return followColor;
    }

    public void setFollowColor(Color followColor) {
        this.followColor = followColor;
    }

    public Color getKeyFrameColor() {
        return keyFrameColor;
    }

    public void setKeyFrameColor(Color keyFrameColor) {
        this.keyFrameColor = keyFrameColor;
    }

    public Color getMouseColor() {
        return mouseColor;
    }

    public void setMouseColor(Color mouseColor) {
        this.mouseColor = mouseColor;
    }

    public Color getStartMarkColor() {
        return startMarkColor;
    }

    public void setStartMarkColor(Color startMarkColor) {
        this.startMarkColor = startMarkColor;
    }

    public Color getEndMarkColor() {
        return endMarkColor;
    }

    public void setEndMarkColor(Color endMarkColor) {
        this.endMarkColor = endMarkColor;
    }

    public Color getMarkOverlayColor() {
        return markOverlayColor;
    }

    public void setMarkOverlayColor(Color markOverlayColor) {
        this.markOverlayColor = markOverlayColor;
    }

    public void setKeyFrames(List<Long> keyFrames) {
        this.keyFrames = keyFrames;
    }

    public void setMediaLengthMicros(long mediaLengthMicros) {
        this.mediaLengthMicros = mediaLengthMicros;
    }

    public void setFps(double fps){
        this.fps = fps;
    }

    public void setFrame(int frame){
        this.frame = frame;
    }
}
