package loli.ui.control.table;

import loli.enumeration.AssEventType;
import loli.helper.AssActor;
import loli.helper.AssEffect;
import loli.helper.AssStyle;
import loli.helper.AssTime;
import loli.io.ASS;
import loli.subtitle.Event;
import loli.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TablePanel extends JPanel {
    private final MainFrame mainFrame;
    private final JTable assTable;
    private final AssTableModel assTableModel;
    private final MainRenderer mainRenderer;

    public TablePanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        assTable = new JTable();
        assTableModel = new AssTableModel();
        assTable.setModel(assTableModel);
        mainRenderer = new MainRenderer();

        assTable.setDefaultRenderer(AssEventType.class, mainRenderer);
        assTable.setDefaultRenderer(AssTime.class, mainRenderer);
        assTable.setDefaultRenderer(AssStyle.class, mainRenderer);
        assTable.setDefaultRenderer(AssActor.class, mainRenderer);
        assTable.setDefaultRenderer(AssEffect.class, mainRenderer);
        assTable.setDefaultRenderer(Event.class, mainRenderer);
        assTable.setDefaultRenderer(String.class, mainRenderer);
        assTable.setDefaultRenderer(Integer.class, mainRenderer);

        setLayout(new BorderLayout());
        add(assTable, BorderLayout.CENTER);

        updateColumnSize();
    }

    public void updateColumnSize(){
        final TableColumnModel cm = assTable.getColumnModel();

        for(int i=0; i<assTable.getColumnCount(); i++){
            switch(i){
                case 0, 6, 7, 8 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 1, 10, 11 -> { cm.getColumn(i).setPreferredWidth(50); }
                case 2, 3 -> { cm.getColumn(i).setPreferredWidth(70); }
                case 4, 5 -> { cm.getColumn(i).setPreferredWidth(130); }
                case 9 -> { cm.getColumn(i).setPreferredWidth(170); }
                case 12 -> { cm.getColumn(i).setPreferredWidth(800); }
            }
        }

        assTable.updateUI();
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public JTable getAssTable() {
        return assTable;
    }

    public AssTableModel getAssTableModel() {
        return assTableModel;
    }

    public MainRenderer getMainRenderer() {
        return mainRenderer;
    }

    public void loadASS(ASS ass){
        assTableModel.setAss(ass);
        assTable.updateUI();
    }

    public ASS saveASS(){
        return assTableModel.getAss();
    }
}
