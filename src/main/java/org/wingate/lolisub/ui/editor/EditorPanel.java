package org.wingate.lolisub.ui.editor;

import org.wingate.lolisub.LoliSub;
import org.wingate.lolisub.MainFrame;
import org.wingate.lolisub.ass.*;
import org.wingate.lolisub.controls.ElementsComboBox;
import org.wingate.lolisub.controls.FlagVersion;
import org.wingate.lolisub.controls.LockFormatTextField;
import org.wingate.lolisub.dialog.SettingsDialog;
import org.wingate.lolisub.helper.DialogResult;
import org.wingate.lolisub.helper.ISO_3166;
import org.wingate.lolisub.helper.Ico;
import org.wingate.lolisub.helper.Msg;
import org.wingate.lolisub.io.Settings;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Locale;

public class EditorPanel extends JPanel {

    private final MainFrame mainFrame;

    private final JTextPane paneOrigin = new JTextPane();
    private final JTextPane paneTranslation = new JTextPane();
    private final JCheckBox cbEditComment = new JCheckBox("#", false);
    private final ElementsComboBox<AssStyle> ecbEditStyles = new ElementsComboBox<>();
    private final ElementsComboBox<AssActor> ecbEditActors = new ElementsComboBox<>();
    private final ElementsComboBox<AssEffect> ecbEditEffects = new ElementsComboBox<>();
    private final JSpinner spinEditLayer = new JSpinner();
    private final SpinnerNumberModel spinEditModelLayer = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final LockFormatTextField lockStart = new LockFormatTextField();
    private final LockFormatTextField lockEnd = new LockFormatTextField();
    private final LockFormatTextField lockDuration = new LockFormatTextField();
    private final JSpinner spinEditML = new JSpinner();
    private final SpinnerNumberModel spinEditModelML = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final JSpinner spinEditMR = new JSpinner();
    private final SpinnerNumberModel spinEditModelMR = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final JSpinner spinEditMV = new JSpinner();
    private final SpinnerNumberModel spinEditModelMV = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final JButton btnAddEventQueue = new JButton();
    private final JButton btnReplaceSelEvent = new JButton();
    private final JButton btnAddEventBefore = new JButton();
    private final JButton btnAddEventAfter = new JButton();
    private FlagVersion flagVersion;

    // TODO: ISO-3166 and translations tasks with and without flagVersion
    public EditorPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());

        JPanel panCommandsOne = new JPanel(new FlowLayout());
        JPanel panCommandsTwo = new JPanel(new FlowLayout());
        JSplitPane splitTextPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        flagVersion = new FlagVersion(
                ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()),
                paneOrigin,
                paneTranslation
        );

        add(panCommandsOne, BorderLayout.NORTH);
        add(splitTextPanes, BorderLayout.CENTER);
        add(panCommandsTwo, BorderLayout.SOUTH);

        panCommandsOne.setPreferredSize(new java.awt.Dimension(panCommandsOne.getWidth(), 30));
        panCommandsTwo.setPreferredSize(new java.awt.Dimension(panCommandsTwo.getWidth(), 30));

        splitTextPanes.setTopComponent(paneOrigin);
        splitTextPanes.setBottomComponent(paneTranslation);

        paneOrigin.setPreferredSize(new java.awt.Dimension(paneOrigin.getWidth(), 50));

        spinEditLayer.setModel(spinEditModelLayer);
        spinEditLayer.setPreferredSize(new java.awt.Dimension(80, 22));
        spinEditML.setModel(spinEditModelML);
        spinEditML.setPreferredSize(new java.awt.Dimension(60, 22));
        spinEditMR.setModel(spinEditModelMR);
        spinEditMR.setPreferredSize(new java.awt.Dimension(60, 22));
        spinEditMV.setModel(spinEditModelMV);
        spinEditMV.setPreferredSize(new java.awt.Dimension(60, 22));

        // TODO Do that carefully:
        ecbEditStyles.addObject(new AssStyle());
        ecbEditActors.addObject(new AssActor());
        ecbEditEffects.addObject(new AssEffect());

        panCommandsOne.add(cbEditComment);
        panCommandsOne.add(ecbEditStyles);
        panCommandsOne.add(ecbEditActors);
        panCommandsOne.add(ecbEditEffects);
        panCommandsOne.add(spinEditLayer);
        panCommandsOne.add(lockStart);
        panCommandsOne.add(lockEnd);
        panCommandsOne.add(lockDuration);
        panCommandsOne.add(spinEditML);
        panCommandsOne.add(spinEditMR);
        panCommandsOne.add(spinEditMV);

        btnAddEventQueue.setIcon(Ico.images("16OK-custom-green.png"));
        btnReplaceSelEvent.setIcon(Ico.images("16OK-custom-orange.png"));
        btnAddEventBefore.setIcon(Ico.images("16OK-custom-blue.png"));
        btnAddEventAfter.setIcon(Ico.images("16OK-custom-violet.png"));

        btnAddEventQueue.setToolTipText(LoliSub.RSX.getString("btnAddEventQueue"));
        btnReplaceSelEvent.setToolTipText(LoliSub.RSX.getString("btnReplaceEvent"));
        btnAddEventBefore.setToolTipText(LoliSub.RSX.getString("btnAddEventBefore"));
        btnAddEventAfter.setToolTipText(LoliSub.RSX.getString("btnAddEventAfter"));

        panCommandsTwo.add(btnAddEventQueue);
        panCommandsTwo.add(btnReplaceSelEvent);
        panCommandsTwo.add(btnAddEventBefore);
        panCommandsTwo.add(btnAddEventAfter);
        panCommandsTwo.add(flagVersion);

        ecbEditStyles.getButton().addActionListener((_)->{
            Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
            SettingsDialog set = new SettingsDialog(mainFrame);
            set.getTabbedPane().setSelectedIndex(1);
            set.getStyleTabbedPane().setSelectedIndex(0);
            set.showDialog(Settings.read(conf.toString()));

            if(set.getDialogResult() == DialogResult.OK){
                Settings.write(conf.toString(), set.getSettings());
            }
        });

        ecbEditActors.getButton().addActionListener((_)->{
            Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
            SettingsDialog set = new SettingsDialog(mainFrame);
            set.getTabbedPane().setSelectedIndex(2);
            set.showDialog(Settings.read(conf.toString()));

            if(set.getDialogResult() == DialogResult.OK){
                Settings.write(conf.toString(), set.getSettings());
            }
        });

        ecbEditEffects.getButton().addActionListener((_)->{
            Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
            SettingsDialog set = new SettingsDialog(mainFrame);
            set.getTabbedPane().setSelectedIndex(3);
            set.showDialog(Settings.read(conf.toString()));

            if(set.getDialogResult() == DialogResult.OK){
                Settings.write(conf.toString(), set.getSettings());
            }
        });

        btnAddEventQueue.addActionListener(e -> {
            try{
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                mainFrame.getTablePanel().getTable().getAssTableModel().addValue(
                        createTextPaneEvent(text)
                );
                mainFrame.getTablePanel().getTable().getAssTable().updateUI();
                mainFrame.getTablePanel().getLeft().repaint();
            }catch(Exception ex){
                Msg.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnReplaceSelEvent.addActionListener(e -> {
            try{
                if(mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow() != -1){
                    String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                    mainFrame.getTablePanel().getTable().getAssTableModel().replaceValueAt(
                            createTextPaneEvent(text),
                            mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow()
                    );
                    mainFrame.getTablePanel().getTable().getAssTable().updateUI();
                    mainFrame.getTablePanel().getLeft().repaint();
                }
            }catch(Exception ex){
                Msg.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnAddEventBefore.addActionListener(e -> {
            try{
                if(mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow() != -1){
                    String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                    mainFrame.getTablePanel().getTable().getAssTableModel().insertValueAt(
                            createTextPaneEvent(text),
                            mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow()
                    );
                    mainFrame.getTablePanel().getTable().getAssTable().updateUI();
                    mainFrame.getTablePanel().getLeft().repaint();
                }
            }catch(Exception ex){
                Msg.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnAddEventAfter.addActionListener(e -> {
            try{
                if(mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow() != -1){
                    String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                    if(mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow() == mainFrame.getTablePanel().getTable().getAssTable().getRowCount() - 1){
                        mainFrame.getTablePanel().getTable().getAssTableModel().addValue(
                                createTextPaneEvent(text)
                        );
                    }else{
                        mainFrame.getTablePanel().getTable().getAssTableModel().insertValueAt(
                                createTextPaneEvent(text),
                                mainFrame.getTablePanel().getTable().getAssTable().getSelectedRow() + 1
                        );
                    }
                    mainFrame.getTablePanel().getTable().getAssTable().updateUI();
                    mainFrame.getTablePanel().getLeft().repaint();
                }
            }catch(Exception ex){
                Msg.dialogErr(ex.getLocalizedMessage());
            }
        });
    }

    private AssEvent createTextPaneEvent(String text){
        AssEvent event = new AssEvent();

        event.setType(cbEditComment.isSelected() ? AssEvent.Type.Comment : AssEvent.Type.Dialogue);
        event.setLayer(spinEditModelLayer.getNumber().intValue()); // layer
        event.setStart(lockStart.getAssTime()); // start
        event.setEnd(lockEnd.getAssTime()); // end
        event.setStyle(ecbEditStyles.getSelectedItem()); // style
        event.setName(ecbEditActors.getSelectedItem()); // name
        event.setMarginL(spinEditModelML.getNumber().intValue()); // marginL
        event.setMarginR(spinEditModelMR.getNumber().intValue()); // marginR
        event.setMarginV(spinEditModelMV.getNumber().intValue()); // marginV
        event.setEffect(ecbEditEffects.getSelectedItem()); // effect
        event.setText(text); // text

        return event;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setToLockStart(AssTime t){
        if(lockStart.isLock()) return;
        lockStart.setAssTime(t);
    }

    public void setToLockEnd(AssTime t){
        if(lockEnd.isLock()) return;
        lockEnd.setAssTime(t);
    }

    public void setToLockDuration(AssTime t){
        if(lockDuration.isLock()) return;
        lockDuration.setAssTime(t);
    }
}
