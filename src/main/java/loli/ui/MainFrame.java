package loli.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import loli.Sub;
import loli.enumeration.DialogResult;
import loli.enumeration.DrawColor;
import loli.helper.OnError;
import loli.helper.OnLoad;
import loli.io.ASS;
import loli.io.Settings;
import loli.ui.control.audiovideo.AudioPanel;
import loli.ui.control.audiovideo.VideoPanel;
import loli.ui.control.audiovideo.VideoPlayer;
import loli.ui.control.audiovideo.WaveFormPanel;
import loli.ui.control.table.ChatPanel;
import loli.ui.control.table.MainRenderer;
import loli.ui.control.table.MainView;
import loli.ui.dialog.AboutDialog;
import loli.ui.dialog.SettingsDialog;
import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {

    private final JLayeredPane topLayers;
    private final JLayeredPane bottomLayers;

    private final VideoPanel videoPanel; // TODO: change this component by a specialized one
    private final AudioPanel audioPanel; // TODO: change this component by a specialized one
    private final MainView tablePanel;
    private final ChatPanel chatPanel;
    private final WaveFormPanel waveFormPanel;
    private final VideoPlayer videoPlayer;

    private int mainWidth = 1000;
    private int mainHeight = 1000;

    public MainFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                mainWidth = e.getComponent().getWidth();
                mainHeight = e.getComponent().getHeight();
                resizeElements();
            }
        });

        setSize(mainWidth, mainHeight);
        // Create main panel on top of Frame
        // Add main panel to the center of Frame
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);
        setJMenuBar(createMainMenu());
        //==============================================================================
        // Set layout of main panel to 2 rows
        // 1st: Video or audio or what we want to visually edit
        // 2nd: ASS table and entry selection
        mainPanel.setLayout(new GridLayout(2, 1, 2, 2));
        // 1st: Panel
        JPanel panEmbedMedia = new JPanel(new BorderLayout());
        // Add two menus on top
        JPanel embedTopMenu = new JPanel(new BorderLayout());
        embedTopMenu.add(createTopLayersMenu(), BorderLayout.CENTER);
        embedTopMenu.add(createTopSwitchMenu(), BorderLayout.EAST);
        // Add menu panel to pan embed media
        panEmbedMedia.add(embedTopMenu, BorderLayout.NORTH);
        // Create top layers handler
        topLayers = new JLayeredPane();
        panEmbedMedia.add(topLayers, BorderLayout.CENTER);
        mainPanel.add(panEmbedMedia); // ::: MAIN PANEL ADDS SOMETHING :::
        // 2nd: layered pane (will be at the bottom)
        JPanel panEmbedLayers = new JPanel(new BorderLayout());
        // Add two menus on top
        JPanel embedBottomMenu = new JPanel(new BorderLayout());
        embedBottomMenu.add(createBottomLayersMenu(), BorderLayout.CENTER);
        embedBottomMenu.add(createBottomSwitchMenu(), BorderLayout.EAST);
        // Add menu panel to pan embed ASS Table
        panEmbedLayers.add(embedBottomMenu, BorderLayout.NORTH);
        // Create bottom layers handler
        bottomLayers = new JLayeredPane();
        panEmbedLayers.add(bottomLayers, BorderLayout.CENTER);
        mainPanel.add(panEmbedLayers); // ::: MAIN PANEL ADDS SOMETHING :::
        //------------------------------------------------------------------------------
        chatPanel = new ChatPanel(this);
        tablePanel = new MainView(this);
        waveFormPanel = new WaveFormPanel(this);
        videoPlayer = new VideoPlayer(this);
        videoPanel = new VideoPanel(this, videoPlayer);
        audioPanel = new AudioPanel(this, videoPlayer);
        //------------------------------------------------------------------------------
        //==============================================================================
        // Top layers (Video)(Audio)(Drawing)(Karaoke What you see is what you get)(+)
        topLayers.add(videoPanel, 0);
        topLayers.add(audioPanel, 1);
        // Hide underlayers elements
        for (Component component : topLayers.getComponents()) {
            component.setVisible(topLayers.getPosition(component) != 1);
        }
        //==============================================================================
        // Two layers:
        // 1st: ASS
        bottomLayers.add(tablePanel, 0);
        // 2nd: Chat
        chatPanel.setBackground(DrawColor.white.getColor(.3f));
        bottomLayers.add(chatPanel, 1);
        // Hide underlayers elements
        for (Component component : bottomLayers.getComponents()) {
            component.setVisible(bottomLayers.getPosition(component) != 1);
        }
    }

    private JMenuBar createMainMenu(){
        final JMenuBar menuBar = new JMenuBar();

        final JMenu mnuLightMode = new JMenu("");
        mnuLightMode.setIcon(OnLoad.images("16 carbon--light-color.png"));
        mnuLightMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try{
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    SwingUtilities.updateComponentTreeUI(MainFrame.this);
                    videoPanel.getTimeBar()
                            .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
                    audioPanel.getAudioTimeBar()
                            .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
                    videoPanel.getTimeBar().repaint();
                    audioPanel.getAudioTimeBar().repaint();
                }catch(Exception exc){
                    OnError.dialogErr(exc.getLocalizedMessage());
                }
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

                try{
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    SwingUtilities.updateComponentTreeUI(MainFrame.this);
                    videoPanel.getTimeBar()
                            .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
                    audioPanel.getAudioTimeBar()
                            .setThemeBackgroundColor(UIManager.getColor("Panel.background"));
                    videoPanel.getTimeBar().repaint();
                    audioPanel.getAudioTimeBar().repaint();
                }catch(Exception exc){
                    OnError.dialogErr(exc.getLocalizedMessage());
                }
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

                Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
                SettingsDialog set = new SettingsDialog(MainFrame.this);
                set.showDialog(Settings.read(conf.toString()));

                if(set.getDialogResult() == DialogResult.OK){
                    Settings.write(conf.toString(), set.getSettings());
                }

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

                AboutDialog about = new AboutDialog(MainFrame.this);
                about.showDialog();

                mnuAbout.setSelected(false);
                mnuAbout.updateUI();
            }
        });
        menuBar.add(mnuAbout);

        return menuBar;
    }

    private JMenuBar createTopLayersMenu() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu mnuFile = new JMenu(Sub.L.getString("mnuFile"));
        menuBar.add(mnuFile);

        final JMenuItem mFileOpenV = new JMenuItem(Sub.L.getString("mFileOpenVideo"));
        mFileOpenV.setIcon(OnLoad.images("20-folder.png"));
        mnuFile.add(mFileOpenV);
        mFileOpenV.addActionListener((e)->{
            JFileChooser fcFile = new JFileChooser();
            fcFile.setAcceptAllFileFilterUsed(false);
            fcFile.setFileFilter(new FileFilter() {
                final java.util.List<String> ext = Arrays.asList(
                        new String[]{".mp4", ".avi", ".m2ts", ".ts", ".mpeg",
                                ".vob", "m4v", "mkv", ".ogm", ".divx",
                                ".mov", ".flv", ".wmv"});

                @Override
                public boolean accept(File f) {
                    if(f.isDirectory()) return true;
                    for(String e : ext){
                        if(f.getName().endsWith(e)) return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return Sub.L.getString("VideoFiles");
                }
            });
            int z = fcFile.showOpenDialog(this);
            if(z == JFileChooser.APPROVE_OPTION){
                try {
                    openVideoMedia(fcFile.getSelectedFile().getPath());
                    // TODO: remove -> openAudioMedia(fcFile.getSelectedFile().getPath());
                } catch (FrameGrabber.Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        final JMenuItem mFileOpenA = new JMenuItem(Sub.L.getString("mFileOpenAudio"));
        mFileOpenA.setIcon(OnLoad.images("16 note2.png"));
        mnuFile.add(mFileOpenA);
        mFileOpenA.addActionListener((e)->{
            JFileChooser fcFile = new JFileChooser();
            fcFile.setAcceptAllFileFilterUsed(false);
            fcFile.setFileFilter(new FileFilter() {
                final List<String> ext = Arrays.asList(
                        new String[]{".mp4", ".mp3", ".m4a", ".aac", ".mp2",
                                ".mp1", "wav", "wma", ".tta", ".snd",
                                ".ogg", ".oga", ".opus", ".mka", ".flac"});

                @Override
                public boolean accept(File f) {
                    if(f.isDirectory()) return true;
                    for(String e : ext){
                        if(f.getName().endsWith(e)) return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return Sub.L.getString("AudioFiles");
                }
            });
            int z = fcFile.showOpenDialog(this);
            if(z == JFileChooser.APPROVE_OPTION){
                try {
                    openAudioMedia(fcFile.getSelectedFile().getPath());
                } catch (FrameGrabber.Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return menuBar;
    }

    private JMenuBar createBottomLayersMenu() {
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
                tablePanel.getTable().loadASS(ass);
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
                ASS ass = tablePanel.getTable().saveASS();
                ASS.write(ass, fcFile.getSelectedFile().getPath());
            }
        });

        JRadioButtonMenuItem mEditNoStrip = new JRadioButtonMenuItem(Sub.L.getString("mEditNoStrip"));
        mnuEdit.add(mEditNoStrip);
        mEditNoStrip.addActionListener((e)->{
            tablePanel.getTable().getMainRenderer().setStripped(MainRenderer.Stripped.Off);
            tablePanel.getTable().getAssTable().updateUI();
        });

        JRadioButtonMenuItem mEditSignStrip = new JRadioButtonMenuItem(Sub.L.getString("mEditSignStrip"));
        mnuEdit.add(mEditSignStrip);
        mEditSignStrip.addActionListener((e)->{
            tablePanel.getTable().getMainRenderer().setStripped(MainRenderer.Stripped.Partially);
            tablePanel.getTable().getAssTable().updateUI();
        });

        JRadioButtonMenuItem mEditStripAll = new JRadioButtonMenuItem(Sub.L.getString("mEditStripAll"));
        mnuEdit.add(mEditStripAll);
        mEditStripAll.addActionListener((e)->{
            tablePanel.getTable().getMainRenderer().setStripped(MainRenderer.Stripped.On);
            tablePanel.getTable().getAssTable().updateUI();
        });

        ButtonGroup bgStripped = new ButtonGroup();
        bgStripped.add(mEditNoStrip);
        bgStripped.add(mEditSignStrip);
        bgStripped.add(mEditStripAll);
        mEditSignStrip.setSelected(true);

        return menuBar;
    }

    private JMenuBar createTopSwitchMenu() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu mnuSwitch = new JMenu(Sub.L.getString("mnuCycle"));
        menuBar.add(mnuSwitch);
        mnuSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                changeLayers(topLayers);
                mnuSwitch.setSelected(false);
                mnuSwitch.updateUI();
            }
        });

        return menuBar;
    }

    private JMenuBar createBottomSwitchMenu() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu mnuSwitch = new JMenu(Sub.L.getString("mnuCycle"));
        menuBar.add(mnuSwitch);
        mnuSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                changeLayers(bottomLayers);
                mnuSwitch.setSelected(false);
                mnuSwitch.updateUI();
            }
        });

        return menuBar;
    }

    private void resizeElements(){
        // Top
        videoPanel.setSize(topLayers.getSize());
        audioPanel.setSize(topLayers.getSize());
        videoPanel.updateUI();
        audioPanel.updateUI();
        // Bottom
        tablePanel.setSize(bottomLayers.getSize());
        chatPanel.setSize(bottomLayers.getSize());
        tablePanel.updateUI();
        chatPanel.updateUI();
    }

    private void changeLayers(JLayeredPane layers){
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

    private void openVideoMedia(String media) throws FrameGrabber.Exception {
        videoPanel.getPlayer().setMediaPath(media, false);
        videoPanel.getTimeBar().setKeyFrames(videoPanel.getPlayer().getFfEngine().getKeyFrames());
        waveFormPanel.open(media);
    }

    private void openAudioMedia(String media) throws FrameGrabber.Exception {
        videoPanel.getPlayer().setMediaPath(media, true);
        waveFormPanel.open(media);
    }

    public VideoPanel getVideoPanel() {
        return videoPanel;
    }

    public AudioPanel getAudioPanel() {
        return audioPanel;
    }

    public MainView getTablePanel() {
        return tablePanel;
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public WaveFormPanel getWaveFormPanel() {
        return waveFormPanel;
    }
}
