package org.wingate.lolisub.controls;

import org.wingate.lolisub.ass.AssTime;
import org.wingate.lolisub.helper.Ico;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class LockFormatTextField extends JPanel {

    public enum Format {
        AssTime, MsTime, Frame;
    }

    public enum Lock {
        NOT, OK;
    }

    private Format format;
    private Lock lock;

    private float fps;
    private AssTime assTime;

    private final ImageIcon formatAssTime;
    private final ImageIcon formatMsTime;
    private final ImageIcon formatFrame;
    private final ImageIcon lockNOT;
    private final ImageIcon lockOK;

    /**
     * Creates new form LockFormatTextField
     */
    public LockFormatTextField() {
        initComponents();

        fps = 24000f / 1001f;
        assTime = new AssTime();
        placeholderTextField1.setPlaceholder(assTime.toAss());

        format = Format.AssTime;
        lock = Lock.NOT;

        formatAssTime = Ico.images("thin-present-blue.png");
        formatMsTime = Ico.images("thin-present-yellow.png");
        formatFrame = Ico.images("thin-present-red.png");
        lockNOT = Ico.images("thin-linkNOT.png");
        lockOK = Ico.images("thin-linkOK.png");

        btnFormat.setIcon(formatAssTime);
        btnLock.setIcon(lockNOT);

        btnFormat.setSize(3, 20);
        btnLock.setSize(10, 20);
        placeholderTextField1.setPreferredSize(new java.awt.Dimension(74, 20));

        btnFormat.addActionListener((e)->{
            changeFormat();
        });

        btnLock.addActionListener((e)->{
            changeLock();
        });

        placeholderTextField1.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                switch(format){
                    case AssTime -> {
                        if(placeholderTextField1.getText().matches("\\d{1}:\\d{2}:\\d{2}.{1}\\d{2}")){
                            assTime = AssTime.create(placeholderTextField1.getText());
                        }
                    }
                    case MsTime -> {
                        if(placeholderTextField1.getText().matches("\\d+.?\\d*")){
                            assTime.setMsTime(Double.parseDouble(placeholderTextField1.getText()));
                        }
                    }
                    case Frame -> {
                        if(placeholderTextField1.getText().matches("\\d+")){
                            int frame = Integer.parseInt(placeholderTextField1.getText());
                            double ms = (double)frame / (double)fps * 1000d;
                            assTime.setMsTime(ms);
                        }
                    }
                }
            }
        });
    }

    public void changeFormat(){
        if(lock == Lock.OK) return;
        String text = "";
        switch(format){
            case AssTime -> {
                format = Format.MsTime;
                btnFormat.setIcon(formatMsTime);
                text = Double.toString(assTime.getMsTime());
                if(text.equals("0")){
                    text = "";
                    placeholderTextField1.setPlaceholder("0");
                }
            }
            case MsTime -> {
                format = Format.Frame;
                btnFormat.setIcon(formatFrame);
                text = Integer.toString((int)(assTime.getMsTime() / 1000d * fps));
                if(text.equals("0")){
                    text = "";
                    placeholderTextField1.setPlaceholder("0");
                }
            }
            case Frame -> {
                format = Format.AssTime;
                btnFormat.setIcon(formatAssTime);
                text = assTime.toAss();
                if(text.equals((new AssTime()).toAss())){
                    text = "";
                    placeholderTextField1.setPlaceholder((new AssTime()).toAss());
                }
            }
        }
        placeholderTextField1.setText(text);
    }

    public void changeLock(){
        switch(lock){
            case NOT -> {
                lock = Lock.OK;
                btnLock.setIcon(lockOK);
                placeholderTextField1.setEditable(false);
            }
            case OK -> {
                lock = Lock.NOT;
                btnLock.setIcon(lockNOT);
                placeholderTextField1.setEditable(true);
            }
        }
    }

    public Format getFormat(){
        return format;
    }

    public void setFormat(Format f, boolean force){
        if(force){
            if(lock == Lock.OK){
                lock = Lock.NOT;
                setFormat(f);
                lock = Lock.OK;
            }else{
                setFormat(f);
            }
        }
    }

    public void setFormat(Format f){
        if(lock == Lock.OK) return;
        String text = "";
        switch(f){
            case AssTime -> {
                format = Format.AssTime;
                btnFormat.setIcon(formatAssTime);
                text = assTime.toAss();
                if(text.equals((new AssTime()).toAss())){
                    text = "";
                    placeholderTextField1.setPlaceholder((new AssTime()).toAss());
                }
            }
            case MsTime -> {
                format = Format.MsTime;
                btnFormat.setIcon(formatMsTime);
                text = Double.toString(assTime.getMsTime());
                if(text.equals("0")){
                    text = "";
                    placeholderTextField1.setPlaceholder("0");
                }
            }
            case Frame -> {
                format = Format.Frame;
                btnFormat.setIcon(formatFrame);
                text = Integer.toString((int)(assTime.getMsTime() / 1000d * fps));
                if(text.equals("0")){
                    text = "";
                    placeholderTextField1.setPlaceholder("0");
                }
            }
        }
        placeholderTextField1.setText(text);
    }

    public Lock getLock(){
        return lock;
    }

    public void setLock(Lock l){
        switch(l){
            case NOT -> {
                lock = Lock.NOT;
                btnLock.setIcon(lockNOT);
                placeholderTextField1.setEditable(true);
            }
            case OK -> {
                lock = Lock.OK;
                btnLock.setIcon(lockOK);
                placeholderTextField1.setEditable(false);
            }
        }
    }

    public boolean isLock(){
        return lock == Lock.OK;
    }

    public PlaceholderTextField getTextField(){
        return placeholderTextField1;
    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
        setFormat(format, true);
    }

    public AssTime getAssTime() {
        return assTime;
    }

    public void setAssTime(AssTime assTime) {
        if(!isLock()){
            this.assTime = assTime;
            setFormat(format);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnFormat = new javax.swing.JButton();
        btnLock = new javax.swing.JButton();
        placeholderTextField1 = new PlaceholderTextField();

        setLayout(new java.awt.BorderLayout());

        btnFormat.setToolTipText("<html>Click in this area to change mode between ass time, ms time and frame.<br>\nBLUE = ass time.<br>\nYELLOW = ms time.<br>\nRED = frame.");
        add(btnFormat, java.awt.BorderLayout.LINE_START);

        btnLock.setToolTipText("Click in this area to enable/disable the lock of the value.");
        add(btnLock, java.awt.BorderLayout.LINE_END);

        placeholderTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        placeholderTextField1.setToolTipText("");
        placeholderTextField1.setPlaceholder("0:00:00.00");
        add(placeholderTextField1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFormat;
    private javax.swing.JButton btnLock;
    private PlaceholderTextField placeholderTextField1;
    // End of variables declaration//GEN-END:variables
}
