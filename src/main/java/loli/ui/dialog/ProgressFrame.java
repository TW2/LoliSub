package loli.ui.dialog;

import loli.Sub;
import loli.helper.OnLoad;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * ProgressFrame shows the progress of a task
 */
public class ProgressFrame extends JFrame implements Runnable {

    private long max = 0L;
    private float percent;
    private final JProgressBar progressBar;

    private Thread thread = null;
    private volatile boolean complete = false;

    /**
     * Open the JFrame
     * @param parent component to center the frame
     */
    public ProgressFrame(Component parent){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setSize(300,40);

        percent = 0f;

        progressBar = new JProgressBar(0, 100); // percent
        progressBar.setStringPainted(true);
        JButton btnInterrupt = new JButton(OnLoad.images("16 cross-small.png"));
        btnInterrupt.setPreferredSize(new Dimension(20, 20)); // Paranoiac use

        JPanel panTop = new JPanel(new BorderLayout());
        Color c = UIManager.getColor("Component.borderColor");
        panTop.setBorder(new TitledBorder(
                new LineBorder(c, 1), Sub.L.getString("progressFrameText"))
        );
        panTop.add(progressBar, BorderLayout.CENTER);
        panTop.add(btnInterrupt, BorderLayout.EAST);

        getContentPane().add(panTop);

        btnInterrupt.addActionListener((_)-> stopNow());

        setLocationRelativeTo(parent);
    }

    /**
     * Show the frame (always on top)
     * @param max your maximum which will be transformed to percent to stop the processing
     */
    public void showLoading(long max){
        this.max = max;

        setVisible(true);
        start();
    }

    /**
     * Start
     */
    private void start(){
        stop();
        thread = new Thread(this);
        complete = false;
        thread.start();
    }

    /**
     * Stop
     */
    private void stop(){
        if(thread != null){
            if(thread.isAlive() || !thread.isInterrupted()){
                thread.interrupt();
                thread = null;
                complete = true;
            }
        }
    }

    /**
     * Stop the thread immediately
     */
    public void stopNow(){
        complete = true;
        stop();
    }

    /**
     * Update the percent (condition is 100 to stop frame)
     * @param value your current value
     * @return a value between 0 and 100 corresponding to the percent
     */
    public int update(long value){
        percent = value != -1L ? (float)((double)value / (double)max) : percent;
        return Math.round(percent * 100);
    }

    @Override
    public void run() {
        while(!complete){
            progressBar.setValue(Math.round(percent * 100));
            if(percent >= 1f){
                stopNow();
            }
        }
    }

    // Example
    public static void main(String[] args) {
        ProgressFrame loading = new ProgressFrame(new JFrame());
        loading.showLoading(3_000L);
        long ms = 0L;
        int ctrl = 0;
        while(ctrl < 100){
            try{
                Thread.sleep(100L);
                ms += 100L;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Return 0 to 100 (percent)
            ctrl = loading.update(ms);
        }
        loading.setVisible(false);
        loading.dispose();
    }
}
