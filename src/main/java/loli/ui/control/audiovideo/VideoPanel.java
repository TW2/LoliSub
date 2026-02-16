package loli.ui.control.audiovideo;

import loli.helper.OnLoad;
import loli.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class VideoPanel extends JPanel {

    private final MainFrame mainFrame;
    private final VideoPlayer player;
    private final VideoTimeBar timeBar;
    private AudioTimeBar audioTimeBar;

    private long microsBeforeAfter;
    private long microsStart;
    private long microsEnd;

    public VideoPanel (MainFrame mainFrame, VideoPlayer videoPlayer){
        this.mainFrame = mainFrame;
        player = videoPlayer;
        timeBar = new VideoTimeBar(true, player);

        player.setVideoTimeBar(timeBar);

        microsBeforeAfter = 250_000L;
        microsStart = 0L;
        microsEnd = 0L;

        JPanel panBottom = new JPanel(new BorderLayout());
        JPanel panCtrl = new JPanel(new FlowLayout());

        setLayout(new BorderLayout());
        add(player, BorderLayout.CENTER);
        add(panBottom, BorderLayout.SOUTH);

        panBottom.add(panCtrl, BorderLayout.WEST);
        panBottom.add(timeBar, BorderLayout.CENTER);

        JButton btnPlay = new JButton(OnLoad.images("16_timer_stuffs play.png"));
        JButton btnPause = new JButton(OnLoad.images("16_timer_stuffs pause.png"));
        JButton btnStop = new JButton(OnLoad.images("16_timer_stuffs stop.png"));
        btnPlay.addActionListener((e)->{
            player.play();
        });
        btnPause.addActionListener((e)->{
            player.pause();
        });
        btnStop.addActionListener((e)->{
            player.stop();
        });
        panCtrl.add(btnPlay);
        panCtrl.add(btnPause);
        panCtrl.add(btnStop);

        JButton btnPlayBefore = new JButton(OnLoad.images("16_timer_stuffs play out 01.png"));
        JButton btnPlayBegin = new JButton(OnLoad.images("16_timer_stuffs play in 01.png"));
        JButton btnPlayArea = new JButton(OnLoad.images("16_timer_stuffs in.png"));
        JButton btnPlayEnd = new JButton(OnLoad.images("16_timer_stuffs play in 02.png"));
        JButton btnPlayAfter = new JButton(OnLoad.images("16_timer_stuffs play out 02.png"));
        btnPlayBefore.addActionListener((e)->{
            long start = Math.max(microsStart - microsBeforeAfter, 0L);
            playArea(start, microsStart);
        });
        btnPlayBegin.addActionListener((e)->{
            playArea(microsStart, microsStart + microsBeforeAfter);
        });
        btnPlayArea.addActionListener((e)->{
            playArea(microsStart, microsEnd);
        });
        btnPlayEnd.addActionListener((e)->{
            playArea(microsEnd - microsBeforeAfter, microsEnd);
        });
        btnPlayAfter.addActionListener((e)->{
            long end = Math.min(microsEnd + microsBeforeAfter, player.getFfEngine().getMediaLength());
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
        if(s < 0L || s > player.getFfEngine().getMediaLength()) return;
        if(e < 0L || e > player.getFfEngine().getMediaLength()) return;

        player.setStartTime(s);
        player.setEndTime(e);
        player.play();
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public VideoPlayer getPlayer() {
        return player;
    }

    public VideoTimeBar getTimeBar() {
        return timeBar;
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
