package loli.ui.dialog;

import loli.Sub;
import loli.enumeration.DialogResult;
import loli.enumeration.HEncodingLanguage;
import loli.helper.AssStyle;
import loli.helper.OnLoad;
import loli.io.Settings;
import loli.renderer.FontRenderer;
import loli.ui.AssStylePreview;
import loli.ui.control.ColorViewer;
import loli.ui.control.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private DialogResult dialogResult;
    private Settings settings;

    // Menu
    private final JTabbedPane tabbedPane;
    private final JTabbedPane styleTabbedPane;

    // Settings folder
    private final JCheckBox chkDefaultFolder;
    private final JTextField tfCustomFolder;

    // Light/Dark mode
    private final JCheckBox chkDarkMode;

    // Colors
    private final DefaultComboBoxModel<String> customColorsModel;
    private final JComboBox<String> cbCustomColors;

    // Project
    private final PlaceholderTextField tfProjectSeason;
    private final PlaceholderTextField tfProjectEpisode;
    private final JTextField tfProjectStylesCol;

    // Style
    private AssStyle assStyle = new AssStyle();

    public SettingsDialog(Frame owner) {
        super(owner, true);
        dialogResult = DialogResult.None;
        setTitle(Sub.L.getString("settings"));

        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);

        JButton btnCancel = new JButton(Sub.L.getString("msgCancel"));
        JButton btnOK = new JButton((Sub.L.getString("msgOK")));
        btnCancel.addActionListener((_)->{
            dialogResult = DialogResult.Cancel;
            setVisible(false);
            dispose();
        });
        btnOK.addActionListener((_)->{
            dialogResult = DialogResult.OK;
            setVisible(false);
            dispose();
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomFirstPanel = new JPanel();
        JPanel bottomLastPanel = new JPanel(new GridLayout(1, 2, 2, 2));
        bottomPanel.add(bottomFirstPanel, BorderLayout.CENTER);
        bottomPanel.add(bottomLastPanel, BorderLayout.EAST);
        bottomLastPanel.add(btnCancel);
        bottomLastPanel.add(btnOK);

        tabbedPane = new JTabbedPane();

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        //------------------------------------------------------------
        // First tab : Main settings
        //------------------------------------------------------------
        JPanel tabMainSettings = new JPanel(null);
        tabbedPane.addTab(
                Sub.L.getString("settingsTitleMain"),
                OnLoad.images("16 carbon--settings-edit.png"),
                tabMainSettings
        );

        chkDefaultFolder = new JCheckBox(Sub.L.getString("settingsMainDefaultFolder"));
        JLabel lblCustomFolder = new JLabel(Sub.L.getString("settingsMainCustomFolder"));
        tfCustomFolder = new JTextField("");
        JButton btnSearchCustomFolder = new JButton("...");
        btnSearchCustomFolder.addActionListener((_)-> {
            JFileChooser fcCustomFolder = new JFileChooser();
            fcCustomFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int z = fcCustomFolder.showOpenDialog(owner);
            if(z == JFileChooser.APPROVE_OPTION){
                tfCustomFolder.setText(fcCustomFolder.getSelectedFile().getAbsolutePath());
            }
        });
        tabMainSettings.add(chkDefaultFolder);
        tabMainSettings.add(lblCustomFolder);
        tabMainSettings.add(tfCustomFolder);
        tabMainSettings.add(btnSearchCustomFolder);
        chkDefaultFolder.setLocation(4,4);
        chkDefaultFolder.setSize(150, 22);
        lblCustomFolder.setLocation(170, 4);
        lblCustomFolder.setSize(150, 22);
        tfCustomFolder.setLocation(336, 4);
        tfCustomFolder.setSize(406, 22);
        btnSearchCustomFolder.setLocation(750, 4);
        btnSearchCustomFolder.setSize(30, 22);

        chkDarkMode = new JCheckBox(Sub.L.getString("settingsDarkMode"));
        tabMainSettings.add(chkDarkMode);
        chkDarkMode.setLocation(4, 30);
        chkDarkMode.setSize(150, 22);

        customColorsModel = new DefaultComboBoxModel<>();
        cbCustomColors = new JComboBox<>(customColorsModel);
        customColorsModel.addElement("TODO"); // TODO
        JButton btnCustomCoors = new JButton(Sub.L.getString("settingsCustomColorChoices"));
        tabMainSettings.add(cbCustomColors);
        tabMainSettings.add(btnCustomCoors);
        btnCustomCoors.setLocation(170, 56);
        btnCustomCoors.setSize(158, 22);
        cbCustomColors.setLocation(336, 56);
        cbCustomColors.setSize(444, 22);

        //------------------------------------------------------------
        // Styles tab :
        //------------------------------------------------------------
        JPanel settingsStylePanelOne = new JPanel(new BorderLayout());
        tabbedPane.addTab(
                Sub.L.getString("settingsTitleStyles"),
                OnLoad.images("16 carbon--character-sentence-case.png"),
                settingsStylePanelOne
        );
        styleTabbedPane = new JTabbedPane();
        AssStylePreview preview = new AssStylePreview();
        preview.setPreferredSize(new Dimension(preview.getWidth(), 60));
        JTextField tfSentence = new JTextField(preview.getSentenceSample());
        tfSentence.addCaretListener((_)->{
            preview.setSentenceSample(assStyle, tfSentence.getText());
        });
        JPanel topStylesPanel = new JPanel(new BorderLayout());
        topStylesPanel.add(preview, BorderLayout.CENTER);
        topStylesPanel.add(tfSentence, BorderLayout.SOUTH);
        settingsStylePanelOne.add(topStylesPanel, BorderLayout.NORTH);
        settingsStylePanelOne.add(styleTabbedPane, BorderLayout.CENTER);
        JPanel firstStyleTab = new JPanel(null);
        styleTabbedPane.addTab(
                Sub.L.getString("settingsTitleStyles0"),
                OnLoad.images("16 penta - rouge.png"),
                firstStyleTab
        );
        JLabel lblStyleName = new JLabel(Sub.L.getString("settingsStyleName"));
        DefaultComboBoxModel<AssStyle> stylesComboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<AssStyle> cbStyles = new JComboBox<>(stylesComboBoxModel);
        cbStyles.addActionListener((_)->{
            if(stylesComboBoxModel.getSize() > 0){
                assStyle = cbStyles.getItemAt(cbStyles.getSelectedIndex());
                preview.setSentenceSample(assStyle, tfSentence.getText());
            }
        });
        JLabel lblEncoding = new JLabel(Sub.L.getString("settingsStyleEncoding"));
        DefaultComboBoxModel<HEncodingLanguage> comboModelEncoding = new DefaultComboBoxModel<>();
        for(HEncodingLanguage enc : HEncodingLanguage.values()){
            comboModelEncoding.addElement(enc);
        }
        comboModelEncoding.setSelectedItem(HEncodingLanguage.Default);
        JComboBox<HEncodingLanguage> cbEncoding = new JComboBox<>(comboModelEncoding);
        cbEncoding.addActionListener((_)->{
            assert comboModelEncoding.getSelectedItem() != null;
            assStyle.setEncoding(((HEncodingLanguage)comboModelEncoding.getSelectedItem()).getCodepage());
            preview.setSentenceSample(assStyle, tfSentence.getText());
        });
        lblStyleName.setLocation(4,4);
        lblStyleName.setSize(130, 22);
        cbStyles.setLocation(138, 4);
        cbStyles.setSize(210, 22);
        lblEncoding.setLocation(356, 4);
        lblEncoding.setSize(170, 22);
        cbEncoding.setLocation(500, 4);
        cbEncoding.setSize(280, 22);
        firstStyleTab.add(lblStyleName);
        firstStyleTab.add(cbStyles);
        firstStyleTab.add(lblEncoding);
        firstStyleTab.add(cbEncoding);

        JLabel lblFontName = new JLabel(Sub.L.getString("settingsStyleFontName"));
        DefaultComboBoxModel<Font> comboModelFontName = new DefaultComboBoxModel<>();
        for(Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()){
            boolean add = true;
            for(int i=0; i<comboModelFontName.getSize(); i++){
                if(f.getFontName().equalsIgnoreCase(
                        comboModelFontName.getElementAt(i).getFontName())){
                    add = false;
                    break;
                }
            }
            if(add){
                comboModelFontName.addElement(f);
            }
        }
        JComboBox<Font> cbFontName = new JComboBox<>(comboModelFontName);
        cbFontName.setRenderer(new FontRenderer());
        cbFontName.addActionListener((_)->{
            assert comboModelFontName.getSelectedItem() != null;
            Font font = new Font(
                    ((Font)comboModelFontName.getSelectedItem()).getFontName(),
                    assStyle.getAssFont().getFont().getStyle(),
                    assStyle.getAssFont().getFont().getSize()
            );
            assStyle.getAssFont().setFont(font.deriveFont(assStyle.getAssFont().getSize()));
            preview.setSentenceSample(assStyle, tfSentence.getText());
        });
        JLabel lblFontSize = new JLabel(Sub.L.getString("settingsStyleFontSize"));
        SpinnerNumberModel spinFontSizeModel = new SpinnerNumberModel(54, 4, 100_000, 1);
        JSpinner spinFontSize = new JSpinner(spinFontSizeModel);
        spinFontSize.addChangeListener((_)->{
            assStyle.getAssFont().setFont(
                    assStyle.getAssFont().getFont().deriveFont(spinFontSizeModel
                            .getNumber().floatValue()
                    )
            );
            preview.setSentenceSample(assStyle, tfSentence.getText());
        });
        JLabel lblFontStyle = new JLabel(Sub.L.getString("settingsStyleFontStyle"));
        JToggleButton tbBold = new JToggleButton(OnLoad.images("16 font style bold.png"));
        JToggleButton tbItalic = new JToggleButton(OnLoad.images("16 font style italic.png"));
        JToggleButton tbUnderline = new JToggleButton(OnLoad.images("16 font style underline.png"));
        JToggleButton tbStrikeOut = new JToggleButton(OnLoad.images("16 font style strikeout.png"));
        lblFontName.setLocation(4, 30);
        lblFontName.setSize(130, 22);
        cbFontName.setLocation(138, 30);
        cbFontName.setSize(210, 22);
        lblFontSize.setLocation(356, 30);
        lblFontSize.setSize(150, 22);
        spinFontSize.setLocation(478, 30);
        spinFontSize.setSize(70, 22);
        lblFontStyle.setLocation(556, 30);
        lblFontStyle.setSize(150, 22);
        tbBold.setLocation(678, 30);
        tbBold.setSize(24, 22);
        tbItalic.setLocation(704, 30);
        tbItalic.setSize(24, 22);
        tbUnderline.setLocation(730, 30);
        tbUnderline.setSize(24, 22);
        tbStrikeOut.setLocation(756, 30);
        tbStrikeOut.setSize(24, 22);
        firstStyleTab.add(lblFontName);
        firstStyleTab.add(cbFontName);
        firstStyleTab.add(lblFontSize);
        firstStyleTab.add(spinFontSize);
        firstStyleTab.add(lblFontStyle);
        firstStyleTab.add(tbBold);
        firstStyleTab.add(tbItalic);
        firstStyleTab.add(tbUnderline);
        firstStyleTab.add(tbStrikeOut);

        JLabel lblL = new JLabel("L :");
        SpinnerNumberModel spinLModel = new SpinnerNumberModel(0, 0, 100000, 1);
        JSpinner spinL = new JSpinner(spinLModel);
        JLabel lblR = new JLabel("R :");
        SpinnerNumberModel spinRModel = new SpinnerNumberModel(0, 0, 100000, 1);
        JSpinner spinR = new JSpinner(spinRModel);
        JLabel lblV = new JLabel("V :");
        SpinnerNumberModel spinVModel = new SpinnerNumberModel(0, 0, 100000, 1);
        JSpinner spinV = new JSpinner(spinVModel);
        lblL.setLocation(4, 56);
        lblL.setSize(20, 22);
        spinL.setLocation(28, 56);
        spinL.setSize(70, 22);
        lblR.setLocation(106, 56);
        lblR.setSize(20, 22);
        spinR.setLocation(138, 56);
        spinR.setSize(70, 22);
        lblV.setLocation(216, 56);
        lblV.setSize(20, 22);
        spinV.setLocation(244, 56);
        spinV.setSize(70, 22);
        firstStyleTab.add(lblL);
        firstStyleTab.add(spinL);
        firstStyleTab.add(lblR);
        firstStyleTab.add(spinR);
        firstStyleTab.add(lblV);
        firstStyleTab.add(spinV);

        JLabel lblAlignment = new JLabel(Sub.L.getString("settingsStyleAlignment"));
        JRadioButton rb1 = new JRadioButton("1");
        JRadioButton rb2 = new JRadioButton("2");
        JRadioButton rb3 = new JRadioButton("3");
        JRadioButton rb4 = new JRadioButton("4");
        JRadioButton rb5 = new JRadioButton("5");
        JRadioButton rb6 = new JRadioButton("6");
        JRadioButton rb7 = new JRadioButton("7");
        JRadioButton rb8 = new JRadioButton("8");
        JRadioButton rb9 = new JRadioButton("9");
        ButtonGroup bgAlignment = new ButtonGroup();
        bgAlignment.add(rb1);
        bgAlignment.add(rb2);
        bgAlignment.add(rb3);
        bgAlignment.add(rb4);
        bgAlignment.add(rb5);
        bgAlignment.add(rb6);
        bgAlignment.add(rb7);
        bgAlignment.add(rb8);
        bgAlignment.add(rb9);
        rb2.setSelected(true);
        lblAlignment.setLocation(4, 82);
        lblAlignment.setSize(100, 22);
        rb7.setLocation(4, 108);
        rb7.setSize(30, 22);
        rb8.setLocation(38, 108);
        rb8.setSize(30, 22);
        rb9.setLocation(72, 108);
        rb9.setSize(30, 22);
        rb4.setLocation(4, 134);
        rb4.setSize(30, 22);
        rb5.setLocation(38, 134);
        rb5.setSize(30, 22);
        rb6.setLocation(72, 134);
        rb6.setSize(30, 22);
        rb1.setLocation(4, 160);
        rb1.setSize(30, 22);
        rb2.setLocation(38, 160);
        rb2.setSize(30, 22);
        rb3.setLocation(72, 160);
        rb3.setSize(30, 22);
        firstStyleTab.add(lblAlignment);
        firstStyleTab.add(rb1);
        firstStyleTab.add(rb2);
        firstStyleTab.add(rb3);
        firstStyleTab.add(rb4);
        firstStyleTab.add(rb5);
        firstStyleTab.add(rb6);
        firstStyleTab.add(rb7);
        firstStyleTab.add(rb8);
        firstStyleTab.add(rb9);

        JLabel lblBorderSize = new JLabel(Sub.L.getString("settingsStyleBorderSize"));
        JLabel lblShadowShift = new JLabel(Sub.L.getString("settingsStyleShadowShift"));
        SpinnerNumberModel spinBorderSizeModel = new SpinnerNumberModel(0, 0, 100000, 1);
        SpinnerNumberModel spinShadowShiftModel = new SpinnerNumberModel(0, 0, 100000, 1);
        JSpinner spinBorderSize = new JSpinner(spinBorderSizeModel);
        JSpinner spinShadowShift = new JSpinner(spinShadowShiftModel);
        JCheckBox chkOpaqueBox = new JCheckBox(Sub.L.getString("settingsStyleOpaqueBox"));
        lblBorderSize.setLocation(136, 108);
        lblBorderSize.setSize(100, 22);
        spinBorderSize.setLocation(244, 108);
        spinBorderSize.setSize(70, 22);
        lblShadowShift.setLocation(136, 134);
        lblShadowShift.setSize(100, 22);
        spinShadowShift.setLocation(244, 134);
        spinShadowShift.setSize(70, 22);
        chkOpaqueBox.setLocation(214, 160);
        chkOpaqueBox.setSize(100, 22);
        firstStyleTab.add(lblBorderSize);
        firstStyleTab.add(spinBorderSize);
        firstStyleTab.add(lblShadowShift);
        firstStyleTab.add(spinShadowShift);
        firstStyleTab.add(chkOpaqueBox);

        JLabel lblScaleX = new JLabel(Sub.L.getString("settingsStyleScaleX"));
        JLabel lblScaleY = new JLabel(Sub.L.getString("settingsStyleScaleY"));
        JLabel lblAngleZ = new JLabel(Sub.L.getString("settingsStyleAngleZ"));
        JLabel lblSpacing = new JLabel(Sub.L.getString("settingsStyleSpacing"));
        SpinnerNumberModel spinScaleXModel = new SpinnerNumberModel(100, 0, 100000, 1);
        JSpinner spinScaleX = new JSpinner(spinScaleXModel);
        SpinnerNumberModel spinScaleYModel = new SpinnerNumberModel(100, 0, 100000, 1);
        JSpinner spinScaleY = new JSpinner(spinScaleYModel);
        SpinnerNumberModel spinAngleZModel = new SpinnerNumberModel(0, -100000, 100000, 1);
        JSpinner spinAngleZ = new JSpinner(spinAngleZModel);
        SpinnerNumberModel spinSpacingModel = new SpinnerNumberModel(0, -100000, 100000, 1);
        JSpinner spinSpacing = new JSpinner(spinSpacingModel);
        lblScaleX.setLocation(322, 56);
        lblScaleX.setSize(100, 22);
        lblScaleY.setLocation(322, 82);
        lblScaleY.setSize(100, 22);
        lblAngleZ.setLocation(322, 108);
        lblAngleZ.setSize(100, 22);
        lblSpacing.setLocation(322, 134);
        lblSpacing.setSize(100, 22);
        firstStyleTab.add(lblScaleX);
        firstStyleTab.add(lblScaleY);
        firstStyleTab.add(lblAngleZ);
        firstStyleTab.add(lblSpacing);
        spinScaleX.setLocation(430, 56);
        spinScaleX.setSize(70, 22);
        spinScaleY.setLocation(430, 82);
        spinScaleY.setSize(70, 22);
        spinAngleZ.setLocation(430, 108);
        spinAngleZ.setSize(70, 22);
        spinSpacing.setLocation(430, 134);
        spinSpacing.setSize(70, 22);
        firstStyleTab.add(spinScaleX);
        firstStyleTab.add(spinScaleY);
        firstStyleTab.add(spinAngleZ);
        firstStyleTab.add(spinSpacing);

        ColorViewer c1 = new ColorViewer(Sub.L.getString("settingsStyleText"), Color.white);
        ColorViewer c2 = new ColorViewer(Sub.L.getString("settingsStyleKaraoke"), Color.yellow);
        ColorViewer c3 = new ColorViewer(Sub.L.getString("settingsStyleOutline"), Color.black);
        ColorViewer c4 = new ColorViewer(Sub.L.getString("settingsStyleShadow"), Color.black);
        c1.setLocation(508, 56);
        c1.setSize(280, 22);
        c2.setLocation(508, 82);
        c2.setSize(280, 22);
        c3.setLocation(508, 108);
        c3.setSize(280, 22);
        c4.setLocation(508, 134);
        c4.setSize(280, 22);
        firstStyleTab.add(c1);
        firstStyleTab.add(c2);
        firstStyleTab.add(c3);
        firstStyleTab.add(c4);

        //------------------------------------------------------------
        // Third tab :
        //------------------------------------------------------------
        JPanel tabActorsSettings = new JPanel(null);
        tabbedPane.addTab(
                Sub.L.getString("settingsTitleActors"),
                OnLoad.images("16 carbon--face-cool.png"),
                tabActorsSettings
        );

        //------------------------------------------------------------
        // Fourth tab :
        //------------------------------------------------------------
        JPanel tabEffectsSettings = new JPanel(null);
        tabbedPane.addTab(
                Sub.L.getString("settingsTitleEffects"),
                OnLoad.images("16 carbon--color-palette.png"),
                tabEffectsSettings
        );

        //------------------------------------------------------------
        // Project tab :
        //------------------------------------------------------------
        JPanel tabProjectSettings = new JPanel(null);
        tabbedPane.addTab(
                Sub.L.getString("settingsTitleProject"),
                OnLoad.images("16 penta - rouge.png"),
                tabProjectSettings
        );
        JLabel lblProjectSeason = new JLabel(Sub.L.getString("settingsSeason"));
        tfProjectSeason = new PlaceholderTextField();
        tfProjectSeason.setPlaceholder(Sub.L.getString("settingsSeasonNaming"));
        JLabel lblProjectEpisode = new JLabel(Sub.L.getString("settingsEpisode"));
        tfProjectEpisode = new PlaceholderTextField();
        tfProjectEpisode.setPlaceholder(Sub.L.getString("settingsEpisodeNaming"));
        tabProjectSettings.add(lblProjectSeason);
        tabProjectSettings.add(tfProjectSeason);
        tabProjectSettings.add(lblProjectEpisode);
        tabProjectSettings.add(tfProjectEpisode);
        lblProjectSeason.setLocation(4, 4);
        lblProjectSeason.setSize(150, 22);
        tfProjectSeason.setLocation(4, 30);
        tfProjectSeason.setSize(150, 22);
        lblProjectEpisode.setLocation(166, 4);
        lblProjectEpisode.setSize(150, 22);
        tfProjectEpisode.setLocation(166,30);
        tfProjectEpisode.setSize(150, 22);

        JLabel lblProjectStyles = new JLabel(Sub.L.getString("settingsStylesCollection"));
        tfProjectStylesCol = new JTextField();
        tabProjectSettings.add(lblProjectStyles);
        tabProjectSettings.add(tfProjectStylesCol);
        lblProjectStyles.setLocation(326, 4);
        lblProjectStyles.setSize(450, 22);
        tfProjectStylesCol.setLocation(326, 30);
        tfProjectStylesCol.setSize(450, 22);
    }

    public void showDialog(Settings settings){
        this.settings = settings;

        chkDefaultFolder.setSelected(settings.isDefaultFolder());
        tfCustomFolder.setText(settings.getCustomFolder() == null ?
                "" : settings.getCustomFolder());
        chkDarkMode.setSelected(settings.isDarkMode());

        tfProjectSeason.setText(settings.getProjectSeason());
        tfProjectEpisode.setText(settings.getProjectEpisode());
        tfProjectStylesCol.setText(settings.getProjectStylesPrefix());

        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public DialogResult getDialogResult(){
        return dialogResult;
    }

    public Settings getSettings() {
        settings.setDefaultFolder(chkDefaultFolder.isSelected());
        settings.setCustomFolder(tfCustomFolder.getText().isEmpty() ?
                null : tfCustomFolder.getText());
        settings.setDarkMode(chkDarkMode.isSelected());

        settings.setProjectSeason(tfProjectSeason.getText());
        settings.setProjectEpisode(tfProjectEpisode.getText());
        settings.setProjectStylesPrefix(tfProjectStylesCol.getText());

        return settings;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JTabbedPane getStyleTabbedPane() {
        return styleTabbedPane;
    }
}
