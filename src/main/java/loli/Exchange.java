package loli;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import loli.enumeration.AssEventType;
import loli.enumeration.DialogResult;
import loli.enumeration.ISO_3166;
import loli.filefilter.AudioFileFilter;
import loli.filefilter.MediaFileFilter;
import loli.helper.AssTime;
import loli.helper.OnError;
import loli.helper.OnLoad;
import loli.io.ASS;
import loli.io.Settings;
import loli.subtitle.Event;
import loli.ui.SubFrame;
import loli.ui.control.audiovideo.AudioPanel;
import loli.ui.control.audiovideo.VideoPanel;
import loli.ui.control.audiovideo.VideoPlayer;
import loli.ui.control.audiovideo.WaveFormPanel;
import loli.ui.control.editor.EditorPanel;
import loli.ui.control.mtable.MTable;
import loli.ui.control.mtable.Voyager;
import loli.ui.dialog.AboutDialog;
import loli.ui.dialog.SettingsDialog;
import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;

public class Exchange {
    /*
     * Cette classe a pour but de lier tous les panneaux ensemble
     * faisant sorte d'éviter les blocages et contre-appels de styles
     * StackOverflow.
     * Elle regroupe les actions et passe les arguments.
     *
     * Le panneau se compose d'un menu en haut (contenu dans un JPanel).
     * L'extrémité droite du menu est une liste déroulante des choix
     * de couches successives des autres panneaux couvrants.
     * Sous ce menu, il y a la section composant les atouts du panneau.
     * Ces atouts peuvent être un ou plusieurs composants assemblés
     * pour pouvoir marcher ensemble.
     *
     * Les panneaux devant tous communiquer comme si on les avait mis
     * à côté, il y a des passerelles entre eux.
     */

    private static SubFrame subFrame;

    private static WaveFormPanel waveFormPanel;
    private static AudioPanel audioPanel;
    private static VideoPanel videoPanel;
    private static EditorPanel editorPanel;
    private static MTable voyagersTable;

    private final JLayeredPane topLayeredPane;
    private final JLayeredPane bottomLayeredPane;

    public Exchange(SubFrame subFrame){
        Exchange.subFrame = subFrame;
        VideoPlayer videoPlayer = new VideoPlayer(this);
        videoPanel = new VideoPanel(this, videoPlayer);
        waveFormPanel = new WaveFormPanel(this);
        audioPanel = new AudioPanel(waveFormPanel, videoPlayer);
        editorPanel = new EditorPanel(this);
        voyagersTable = new MTable(this);

        topLayeredPane = new JLayeredPane();
        bottomLayeredPane = new JLayeredPane();

        // Doit être chargé en dernier.
        // Classes de Exchange
        Video video = new Video(this);
        Audio audio = new Audio(this);
        Table table = new Table(this);

        // On ajuste les layers.
        // TOP ================================================================
        topLayeredPane.add(video, 0);
        topLayeredPane.add(audio, 1);
        // Hide underlayers elements
        for (Component component : topLayeredPane.getComponents()) {
            component.setVisible(topLayeredPane.getPosition(component) != 1);
        }
        // BOTTOM =============================================================
        bottomLayeredPane.add(table, 0);
        // Hide underlayers elements
        for (Component component : bottomLayeredPane.getComponents()) {
            component.setVisible(bottomLayeredPane.getPosition(component) != 1);
        }
    }

    public JMenuBar createMainMenu(){
        final JMenuBar menuBar = new JMenuBar();

        final JMenu mnuLightMode = new JMenu("");
        mnuLightMode.setIcon(OnLoad.images("16 carbon--light-color.png"));
        mnuLightMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mnuLightModeMouseClicked(e);
                mnuLightMode.setSelected(false);
                mnuLightMode.updateUI();
            }
        });
        menuBar.add(mnuLightMode);

        final JMenu mnuDarkMode = new JMenu("");
        mnuDarkMode.setIcon(OnLoad.images("16 carbon--moon-color.png"));
        mnuDarkMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mnuDarkModeMouseClicked(e);
                mnuDarkMode.setSelected(false);
                mnuDarkMode.updateUI();
            }
        });
        menuBar.add(mnuDarkMode);

        final JMenu mnuSettings = new JMenu("");
        mnuSettings.setIcon(OnLoad.images("16 carbon--settings-green.png"));
        mnuSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mnuSettingsMouseClicked(e);
                mnuSettings.setSelected(false);
                mnuSettings.updateUI();
            }
        });
        menuBar.add(mnuSettings);

        final JMenu mnuAutomation = new JMenu("");
        mnuAutomation.setIcon(OnLoad.images("16 carbon--chemistry.png"));
        JMenu mnuPlugins = new JMenu("Plugins");
        mnuPlugins.setIcon(OnLoad.images("16 carbon--settings-green.png"));

        menuBar.add(mnuAutomation);
        mnuAutomation.add(mnuPlugins);

        final JMenu mnuAbout = new JMenu("");
        mnuAbout.setIcon(OnLoad.images("16 carbon--cafe.png"));
        mnuAbout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mnuAboutMouseClicked(e);
                mnuAbout.setSelected(false);
                mnuAbout.updateUI();
            }
        });
        menuBar.add(mnuAbout);

        return menuBar;
    }

    public JLayeredPane getTopLayeredPane() {
        return topLayeredPane;
    }

    public JLayeredPane getBottomLayeredPane() {
        return bottomLayeredPane;
    }

    public static class Video extends JPanel {
        private final Exchange exchange;

        public Video(Exchange exchange) {
            // Regroupe le player et le composant vidéo.
            this.exchange = exchange;
            setLayout(new BorderLayout());
            JPanel menuPlus = new JPanel(new BorderLayout());
            JButton button = new JButton(Sub.L.getString("mnuCycle"));
            button.addActionListener(this::buttonActionListener);
            menuPlus.add(createMenuBar(), BorderLayout.CENTER);
            menuPlus.add(button, BorderLayout.EAST);
            add(menuPlus, BorderLayout.NORTH);
            add(videoPanel, BorderLayout.CENTER);
        }

        private void buttonActionListener(ActionEvent e) {
            exchange.changeLayers(exchange.getTopLayeredPane());
        }

        private JMenuBar createMenuBar() {
            final JMenuBar menuBar = new JMenuBar();

            final JMenu mnuFile = new JMenu(Sub.L.getString("mnuFile"));
            menuBar.add(mnuFile);

            final JMenuItem mFileOpenV = new JMenuItem(Sub.L.getString("mFileOpenVideo"));
            mFileOpenV.setIcon(OnLoad.images("20-folder.png"));
            mnuFile.add(mFileOpenV);
            mFileOpenV.addActionListener(this::mFileOpenVActionPerformed);

            return menuBar;
        }

        public void mFileOpenVActionPerformed(ActionEvent e) {
            JFileChooser fcFile = new JFileChooser();
            fcFile.setAcceptAllFileFilterUsed(false);
            fcFile.setFileFilter(new MediaFileFilter());
            int z = fcFile.showOpenDialog(this);
            if(z == JFileChooser.APPROVE_OPTION){
                openVideoMedia(fcFile.getSelectedFile().getAbsolutePath());
                // TODO: remove -> openAudioMedia(fcFile.getSelectedFile().getPath());
            }
        }

        private void openVideoMedia(String media) {
            try{
                videoPanel.getPlayer().setMediaPath(media, false);
                videoPanel.getTimeBar().setKeyFrames(videoPanel.getPlayer().getFfEngine().getKeyFrames());
                waveFormPanel.open(media);
            }catch(FrameGrabber.Exception ex){
                OnError.dialogErr(ex.getMessage());
            }
        }
    }

    public static class Audio extends JPanel {
        private final Exchange exchange;

        public Audio(Exchange exchange) {
            // Regroupe le composant audio en coordination de la vidéo
            // afin de pouvoir jouer l'audio.
            this.exchange = exchange;
            setLayout(new BorderLayout());
            JPanel menuPlus = new JPanel(new BorderLayout());
            JButton button = new JButton(Sub.L.getString("mnuCycle"));
            button.addActionListener(this::buttonActionListener);
            menuPlus.add(createMenuBar(), BorderLayout.CENTER);
            menuPlus.add(button, BorderLayout.EAST);
            add(menuPlus, BorderLayout.NORTH);
            add(audioPanel, BorderLayout.CENTER);
        }

        private void buttonActionListener(ActionEvent e) {
            exchange.changeLayers(exchange.getTopLayeredPane());
        }

        private JMenuBar createMenuBar() {
            final JMenuBar menuBar = new JMenuBar();

            final JMenu mnuFile = new JMenu(Sub.L.getString("mnuFile"));
            menuBar.add(mnuFile);

            final JMenuItem mFileOpenA = new JMenuItem(Sub.L.getString("mFileOpenAudio"));
            mFileOpenA.setIcon(OnLoad.images("16 note2.png"));
            mnuFile.add(mFileOpenA);
            mFileOpenA.addActionListener(this::mFileOpenAActionListener);

            return menuBar;
        }

        public void mFileOpenAActionListener(ActionEvent e) {
            JFileChooser fcFile = new JFileChooser();
            fcFile.setAcceptAllFileFilterUsed(false);
            fcFile.setFileFilter(new AudioFileFilter());
            int z = fcFile.showOpenDialog(subFrame);
            if(z == JFileChooser.APPROVE_OPTION){
                try {
                    openAudioMedia(fcFile.getSelectedFile().getPath());
                } catch (FrameGrabber.Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        private void openAudioMedia(String media) throws FrameGrabber.Exception {
            videoPanel.getPlayer().setMediaPath(media, true);
            waveFormPanel.open(media);
        }
    }

    public static class Table extends JPanel {
        private final Exchange exchange;

        public Table(Exchange exchange) {
            // Contient les voyageurs (MTable) et l'éditeur.
            this.exchange = exchange;
            setLayout(new BorderLayout());
            JPanel menuPlus = new JPanel(new BorderLayout());
            JButton button = new JButton(Sub.L.getString("mnuCycle"));
            button.addActionListener(this::buttonActionListener);
            menuPlus.add(createMenuBar(), BorderLayout.CENTER);
            menuPlus.add(button, BorderLayout.EAST);
            add(menuPlus, BorderLayout.NORTH);
            JPanel tablePanel = new JPanel(new GridLayout(2, 1));
            tablePanel.add(editorPanel);
            tablePanel.add(voyagersTable);
            add(tablePanel, BorderLayout.CENTER);
        }

        private void buttonActionListener(ActionEvent e) {
            exchange.changeLayers(exchange.getBottomLayeredPane());
        }

        private JMenuBar createMenuBar() {
            final JMenuBar menuBar = new JMenuBar();

            final JMenu mnuFile = new JMenu(Sub.L.getString("mnuFile"));
            menuBar.add(mnuFile);
            final JMenu mnuEdit = new JMenu(Sub.L.getString("mnuEdit"));
            menuBar.add(mnuEdit);

            final JMenuItem mFileOpenASS = new JMenuItem(Sub.L.getString("mFileOpenASS"));
            mFileOpenASS.setIcon(OnLoad.images("20-folder.png"));
            mnuFile.add(mFileOpenASS);
            mFileOpenASS.addActionListener((e)->{
                JFileChooser fcFile = new JFileChooser();
                fcFile.setAcceptAllFileFilterUsed(false);
                fcFile.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith(".ass");
                    }

                    @Override
                    public String getDescription() {
                        return Sub.L.getString("fFilter");
                    }
                });
                int z = fcFile.showOpenDialog(this);
                if(z == JFileChooser.APPROVE_OPTION){
                    ASS ass = ASS.read(fcFile.getSelectedFile().getPath());
                    //tablePanel.getTable().loadASS(ass);
                }
            });

            final JMenuItem mFileSaveASS = new JMenuItem(Sub.L.getString("mFileSaveASS"));
            mFileSaveASS.setIcon(OnLoad.images("20-floppydisk.png"));
            mnuFile.add(mFileSaveASS);
            mFileSaveASS.addActionListener((e)->{
                JFileChooser fcFile = new JFileChooser();
                fcFile.setAcceptAllFileFilterUsed(false);
                fcFile.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith(".ass");
                    }

                    @Override
                    public String getDescription() {
                        return Sub.L.getString("fFilter");
                    }
                });
                int z = fcFile.showSaveDialog(this);
                if(z == JFileChooser.APPROVE_OPTION){
                    //ASS ass = tablePanel.getTable().saveASS();
                    //ASS.write(ass, fcFile.getSelectedFile().getPath());
                }
            });

            JRadioButtonMenuItem mEditNoStrip = new JRadioButtonMenuItem(Sub.L.getString("mEditNoStrip"));
            mnuEdit.add(mEditNoStrip);
            mEditNoStrip.addActionListener((e)->{
                voyagersTable.setStripped(MTable.Stripped.Off);
            });

            JRadioButtonMenuItem mEditSignStrip = new JRadioButtonMenuItem(Sub.L.getString("mEditSignStrip"));
            mnuEdit.add(mEditSignStrip);
            mEditSignStrip.addActionListener((e)->{
                voyagersTable.setStripped(MTable.Stripped.Partially);
            });

            JRadioButtonMenuItem mEditStripAll = new JRadioButtonMenuItem(Sub.L.getString("mEditStripAll"));
            mnuEdit.add(mEditStripAll);
            mEditStripAll.addActionListener((e)->{
                voyagersTable.setStripped(MTable.Stripped.On);
            });

            ButtonGroup bgStripped = new ButtonGroup();
            bgStripped.add(mEditNoStrip);
            bgStripped.add(mEditSignStrip);
            bgStripped.add(mEditStripAll);
            mEditSignStrip.setSelected(true);

            return menuBar;
        }

        private void loadASS(ASS ass){

        }

        private void saveASS(){

//            if(iso == null){
//
//            }else{
//
//            }
        }
    }

    public void mnuLightModeMouseClicked(MouseEvent e) {
        try{
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(subFrame);
            videoPanel.getTimeBar()
                    .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
            audioPanel.getAudioTimeBar()
                    .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
            videoPanel.getTimeBar().repaint();
            audioPanel.getAudioTimeBar().repaint();
        }catch(Exception exc){
            OnError.dialogErr(exc.getLocalizedMessage());
        }
    }

    public void mnuDarkModeMouseClicked(MouseEvent e) {
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(subFrame);
            videoPanel.getTimeBar()
                    .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
            audioPanel.getAudioTimeBar()
                    .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
            videoPanel.getTimeBar().repaint();
            audioPanel.getAudioTimeBar().repaint();
        }catch(Exception exc){
            OnError.dialogErr(exc.getLocalizedMessage());
        }
    }

    public void mnuSettingsMouseClicked(MouseEvent e) {
        Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
        SettingsDialog set = new SettingsDialog(subFrame);
        set.showDialog(Settings.read(conf.toString()));

        if(set.getDialogResult() == DialogResult.OK){
            Settings.write(conf.toString(), set.getSettings());
        }
    }

    public void mnuAboutMouseClicked(MouseEvent e) {
        AboutDialog about = new AboutDialog(subFrame);
        about.showDialog();
    }

    public void changeLayers(JLayeredPane layers){
        Component[] components = layers.getComponents();
        if(components.length == 1) return;

        // Find the on top
        int lastFound = -1;
        for(int i=0; i<components.length; i++){
            if(layers.getPosition(components[i]) == 0){
                lastFound = i;
            }
        }
        int s = lastFound + 1 == components.length ? 0 : lastFound + 1;

        // Select next
        for(int i=0; i<components.length; i++){
            layers.setPosition(components[i], i == s ? 0 : 1);
        }

        // Hide underlayers elements
        for (Component component : components) {
            component.setVisible(layers.getPosition(component) != 1);
        }
    }

    public void defineStart(AssTime t){
        editorPanel.setToLockStart(t); // OK
        audioPanel.setMicrosStart((long) t.getMsTime() * 1_000L); // OK
        videoPanel.setMicrosStart((long) t.getMsTime() * 1_000L); // OK
    }

    public void defineEnd(AssTime t){
        editorPanel.setToLockEnd(t); // OK
        audioPanel.setMicrosEnd((long) t.getMsTime() * 1_000L); // OK
        videoPanel.setMicrosEnd((long) t.getMsTime() * 1_000L); // OK
    }

    public void addEvent(loli.subtitle.Event event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.addEvent(event, f1, f2);
    }

    public void replaceEvent(loli.subtitle.Event event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.replaceEvent(event, f1, f2);
    }

    public void beforeEvent(loli.subtitle.Event event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.beforeEvent(event, f1, f2);
    }

    public void afterEvent(Event event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.afterEvent(event, f1, f2);
    }
}
