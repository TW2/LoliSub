package loli.ui.control.audiovideo;

import loli.helper.OnError;
import loli.helper.OnLoad;

import javax.swing.*;
import java.awt.*;

public class AudioPanel extends JPanel {

    private final AudioTimeBar audioTimeBar;
    private final VideoPlayer videoPlayer;

    private final JScrollBar scrollH;

    private long microsBeforeAfter;
    private long microsStart;
    private long microsEnd;

    public AudioPanel(WaveFormPanel waveFormPanel, VideoPlayer videoPlayer){
        this.videoPlayer = videoPlayer;

        scrollH = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 1_000_000);

        audioTimeBar = new AudioTimeBar(true, this);
        videoPlayer.setAudioTimeBar(audioTimeBar);

        microsBeforeAfter = 250_000L;
        microsStart = 0L;
        microsEnd = 0L;

        JPanel panBottom = new JPanel(new BorderLayout());
        JPanel panCtrl = new JPanel(new FlowLayout());

        JPanel audioWithScroll = new JPanel(new BorderLayout());
        audioWithScroll.add(waveFormPanel, BorderLayout.CENTER);
        audioWithScroll.add(scrollH, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(audioWithScroll, BorderLayout.CENTER);
        add(panBottom, BorderLayout.SOUTH);

        panBottom.add(panCtrl, BorderLayout.WEST);
        panBottom.add(audioTimeBar, BorderLayout.CENTER);

        scrollH.addAdjustmentListener((e) -> {
            try {
                int max = scrollH.getMaximum();
                long microsLength = waveFormPanel.getDurationMicros();
                long offset = e.getValue() * microsLength / max;
                waveFormPanel.update(offset, 2_000_000L, 1d);
            }catch(Exception ex) {
                OnError.consoleErr(ex.getLocalizedMessage());
            }
        });

        JButton btnPlay = new JButton(OnLoad.images("16_timer_stuffs play.png"));
        JButton btnPause = new JButton(OnLoad.images("16_timer_stuffs pause.png"));
        JButton btnStop = new JButton(OnLoad.images("16_timer_stuffs stop.png"));
        btnPlay.addActionListener((_)-> videoPlayer.play());
        btnPause.addActionListener((_)-> videoPlayer.pause());
        btnStop.addActionListener((_)-> videoPlayer.stop());
        panCtrl.add(btnPlay);
        panCtrl.add(btnPause);
        panCtrl.add(btnStop);

        JButton btnPlayBefore = new JButton(OnLoad.images("16_timer_stuffs play out 01.png"));
        JButton btnPlayBegin = new JButton(OnLoad.images("16_timer_stuffs play in 01.png"));
        JButton btnPlayArea = new JButton(OnLoad.images("16_timer_stuffs in.png"));
        JButton btnPlayEnd = new JButton(OnLoad.images("16_timer_stuffs play in 02.png"));
        JButton btnPlayAfter = new JButton(OnLoad.images("16_timer_stuffs play out 02.png"));
        btnPlayBefore.addActionListener((_)->{
            long start = Math.max(microsStart - microsBeforeAfter, 0L);
            playArea(start, microsStart);
        });
        btnPlayBegin.addActionListener((_)->{
            playArea(microsStart, microsStart + microsBeforeAfter);
        });
        btnPlayArea.addActionListener((_)->{
            playArea(microsStart, microsEnd);
        });
        btnPlayEnd.addActionListener((_)->{
            playArea(microsEnd - microsBeforeAfter, microsEnd);
        });
        btnPlayAfter.addActionListener((_)->{
            long end = Math.min(microsEnd + microsBeforeAfter, videoPlayer.getFfEngine().getMediaLength());
            playArea(microsEnd, end);
        });
        panCtrl.add(btnPlayBefore);
        panCtrl.add(btnPlayBegin);
        panCtrl.add(btnPlayArea);
        panCtrl.add(btnPlayEnd);
        panCtrl.add(btnPlayAfter);
    }

    private void playArea(long s, long e){
        if(s == e) return;
        if(s < 0L || s > videoPlayer.getFfEngine().getMediaLength()) return;
        if(e < 0L || e > videoPlayer.getFfEngine().getMediaLength()) return;

        videoPlayer.setStartTime(s);
        videoPlayer.setEndTime(e);
        videoPlayer.play();
    }

    public VideoPlayer getVideoPlayer() {
        return videoPlayer;
    }

    public AudioTimeBar getAudioTimeBar() {
        return audioTimeBar;
    }

    public JScrollBar getScrollH() {
        return scrollH;
    }

    public long getMicrosBeforeAfter() {
        return microsBeforeAfter;
    }

    public void setMicrosBeforeAfter(long microsBeforeAfter) {
        this.microsBeforeAfter = microsBeforeAfter;
    }

    public long getMicrosStart() {
        return microsStart;
    }

    public void setMicrosStart(long microsStart) {
        this.microsStart = microsStart;
    }

    public long getMicrosEnd() {
        return microsEnd;
    }

    public void setMicrosEnd(long microsEnd) {
        this.microsEnd = microsEnd;
    }
}
