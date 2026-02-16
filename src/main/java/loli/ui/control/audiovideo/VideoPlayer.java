package loli.ui.control.audiovideo;

import loli.ui.MainFrame;
import loli.ui.control.FFMpeg;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoPlayer extends javax.swing.JPanel {

    private final MainFrame mainFrame;

    private VideoTimeBar videoTimeBar;
    private AudioTimeBar audioTimeBar;

    private final FFMpeg ffEngine;
    private BufferedImage image;
    private long currentMicros;
    private double fps;
    private int frame;

    public VideoPlayer(MainFrame mainFrame) {
        super(true);
        this.mainFrame = mainFrame;

        videoTimeBar = null;
        audioTimeBar = null;
        ffEngine = new FFMpeg();
        image = null;
        currentMicros = 0L;
        fps = 24000d / 1001d;
        frame = 0;

        ffEngine.addMediaListener(new FFMpeg.FFMessageListener() {
            @Override
            public void updatedMessage(FFMpeg.FFMessage e) {
                // For this element, do that:
                image = e.getImage();
                currentMicros = e.getCurrentMicro();
                fps = e.getFps();
                frame = e.getFrame();
                repaint();

                // For video time bar, do that:
                if(videoTimeBar != null){
                    videoTimeBar.setKeyFrames(ffEngine.getKeyFrames());
                    videoTimeBar.setMediaLengthMicros(ffEngine.getMediaLength());
                    videoTimeBar.setCurrentMicros(e.getCurrentMicro());
                    videoTimeBar.setFps(e.getFps());
                    videoTimeBar.setFrame(e.getFrame());
                    videoTimeBar.repaint();
                }

                // For audio time bar, do that:
                if(audioTimeBar != null){
                    audioTimeBar.setKeyFrames(ffEngine.getKeyFrames());
                    audioTimeBar.setMediaLengthMicros(ffEngine.getMediaLength());
                    audioTimeBar.setCurrentMicros(e.getCurrentMicro());
                    audioTimeBar.setFps(e.getFps());
                    audioTimeBar.setFrame(e.getFrame());
                    audioTimeBar.repaint();
                }
            }
        });
    }

    public void setMediaPath(String mediaPath, boolean audioOnly) {
        ffEngine.setMedia(mediaPath, audioOnly);
    }

    public void play(){
        ffEngine.play();
    }

    public void pause(){
        ffEngine.pause();
    }

    public void stop(){
        ffEngine.stop();
    }

    public void setStartTime(long micros){
        ffEngine.setStartTime(micros);
    }

    public void setEndTime(long micros){
        ffEngine.setEndTime(micros);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0,0, getWidth(), getHeight());

        if(image != null){
            double ratioX = (double) getWidth() / image.getWidth();
            double ratioY = (double) getHeight() / image.getHeight();

            double reducCoeff;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            int w = (int) (image.getWidth() * reducCoeff);
            int h = (int) (image.getHeight() * reducCoeff);

            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;

            g.drawImage(image, x, y, w, h, null);
        }
    }

    public FFMpeg getFfEngine() {
        return ffEngine;
    }

    public BufferedImage getImage() {
        return image;
    }

    public long getCurrentMicros() {
        return currentMicros;
    }

    public double getFps() {
        return fps;
    }

    public int getFrame() {
        return frame;
    }

    public void setVideoTimeBar(VideoTimeBar videoTimeBar) {
        this.videoTimeBar = videoTimeBar;
    }

    public void setAudioTimeBar(AudioTimeBar audioTimeBar) {
        this.audioTimeBar = audioTimeBar;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
