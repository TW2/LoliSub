package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.LoliSub;
import org.wingate.lolisub.MainFrame;
import org.wingate.lolisub.ass.ASS;
import org.wingate.lolisub.ass.AssEvent;
import org.wingate.lolisub.helper.Clipboard;
import org.wingate.lolisub.ui.editor.EditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * MainView is JPanel used for Table and a left component to drive Table
 */
public class MainView extends JPanel {

    private final MainFrame mainFrame;

    //----------------------------------------------------
    // Editor element (external use) in ui.editor package
    private final EditorPanel editor;
    //----------------------------------------------------
    // ASS Table elements
    private final TablePanel table;
    private final LeftComponent left;
    //----------------------------------------------------

    private Point mouseAtContextMenu = null;
    private Class<?> lastBookmarkClass = CommentBookmark.class;

    public MainView(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(2, 1, 2, 2));
        editor = new EditorPanel(mainFrame);
        add(editor);
        JPanel assTableEmbed = new JPanel(new BorderLayout());
        add(assTableEmbed);
        table = new TablePanel(mainFrame);
        left = new LeftComponent(this);
        JScrollPane scrollPane = new JScrollPane(table.getAssTable());
        assTableEmbed.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setRowHeaderView(left);
        JPopupMenu contextMenu = new JPopupMenu();
        table.getAssTable().setComponentPopupMenu(contextMenu);

        scrollPane.getVerticalScrollBar().addAdjustmentListener((e)-> {
            left.setYOffset(e.getAdjustable().getValue());
            left.repaint();
        });

        table.getAssTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getButton() == MouseEvent.BUTTON3){
                    mouseAtContextMenu = e.getPoint();
                }
            }
        });

        JMenuItem mCutLine = new JMenuItem(LoliSub.RSX.getString("mCutLine"));
        ImageIcon iCutLine = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/20px-Crystal_Clear_action_editcut.png")));
        mCutLine.setIcon(iCutLine);
        contextMenu.add(mCutLine);
        mCutLine.addActionListener((e)->{
            if(mouseAtContextMenu == null) return;
            int r = table.getAssTable().rowAtPoint(mouseAtContextMenu);
            Object obj = table.getAssTableModel().getValueAt(r, 12);
            if(obj instanceof AssEvent event){
                Clipboard.copyString(event.toRawLine());
                table.getAssTableModel().removeValueAt(r);
            }
        });

        JMenuItem mCopyLine = new JMenuItem(LoliSub.RSX.getString("mCopyLine"));
        ImageIcon iCopyLine = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/20px-Crystal_Clear_action_editcopy.png")));
        mCopyLine.setIcon(iCopyLine);
        contextMenu.add(mCopyLine);
        mCopyLine.addActionListener((e)->{
            if(mouseAtContextMenu == null) return;
            int r = table.getAssTable().rowAtPoint(mouseAtContextMenu);
            Object obj = table.getAssTableModel().getValueAt(r, 12);
            if(obj instanceof AssEvent event){
                Clipboard.copyString(event.toRawLine());
            }
        });

        JMenuItem mPasteLine = new JMenuItem(LoliSub.RSX.getString("mPasteLine"));
        ImageIcon iPasteLine = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/20px-Crystal_Clear_action_editpaste.png")));
        mPasteLine.setIcon(iPasteLine);
        contextMenu.add(mPasteLine);
        mPasteLine.addActionListener((e)->{
            if(mouseAtContextMenu == null) return;
            int r = table.getAssTable().rowAtPoint(mouseAtContextMenu);
            try {
                String s = Clipboard.pasteString();
                ASS ass = table.getAssTableModel().getAss();
                AssEvent event = AssEvent.createFromRawLine(s, ass);
                table.getAssTableModel().insertValueAt(event, r);
            }catch(Exception _){ }
        });

        contextMenu.addSeparator();

        JMenuItem mDuplicate = new JMenuItem(LoliSub.RSX.getString("mDuplicate"));
        ImageIcon iDuplicate = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/funsub-dupliquer.png")));
        mDuplicate.setIcon(iDuplicate);
        contextMenu.add(mDuplicate);
        mDuplicate.addActionListener((e)->{
            try {
                int[] indices = table.getAssTable().getSelectedRows();
                int last = indices[indices.length - 1];
                for(int i=indices.length-1; i>=0; i--){
                    Object obj = table.getAssTableModel().getValueAt(indices[i], 12);
                    if(obj instanceof AssEvent event){
                        if(last + 1 >= table.getAssTableModel().getRowCount()){
                            table.getAssTableModel().addValue(event);
                        }else{
                            table.getAssTableModel().insertValueAt(event, last + 1);
                        }
                    }
                }
            }catch(Exception _){ }
        });

        contextMenu.addSeparator();

        JMenu mnuBookmark = new JMenu(LoliSub.RSX.getString("mnuBookmark"));
        ImageIcon iBookmark = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/20 bookmark.png")));
        mnuBookmark.setIcon(iBookmark);
        contextMenu.add(mnuBookmark);

        JMenuItem mBookmark = new JMenuItem(LoliSub.RSX.getString("mBookmark"));
        mBookmark.setIcon(iBookmark);
        mnuBookmark.add(mBookmark);
        mBookmark.addActionListener((e)->{
            if(mouseAtContextMenu == null) return;
            int r = table.getAssTable().rowAtPoint(mouseAtContextMenu);
            AssEvent event = (AssEvent)table.getAssTableModel().getValueAt(r, 12);
            Bookmark s = null;
            for(Bookmark b : event.getBookmarks()){
                switch(b){
                    case CommentBookmark b1 -> {if(b.getLine() == r) { s = b1; }}
                    case TransBookmark b1 -> {if(b.getLine() == r) { s = b1; }}
                    case CheckBookmark b1 -> {if(b.getLine() == r) { s = b1; }}
                    case SynchroBookmark b1 -> {if(b.getLine() == r) { s = b1; }}
                    case OtherBookmark b1 -> {if(b.getLine() == r) { s = b1; }}
                    default -> {}
                }
            }

            if(lastBookmarkClass == CommentBookmark.class){
                if(s instanceof CommentBookmark){
                    event.getBookmarks().remove(s);
                }else {
                    event.getBookmarks().add(new CommentBookmark(r));
                }
            }else if(lastBookmarkClass == TransBookmark.class){
                if(s instanceof TransBookmark){
                    event.getBookmarks().remove(s);
                }else {
                    event.getBookmarks().add(new TransBookmark(r));
                }
            }else if(lastBookmarkClass == CheckBookmark.class){
                if(s instanceof CheckBookmark){
                    event.getBookmarks().remove(s);
                }else {
                    event.getBookmarks().add(new CheckBookmark(r));
                }
            }else if(lastBookmarkClass == SynchroBookmark.class){
                if(s instanceof SynchroBookmark){
                    event.getBookmarks().remove(s);
                }else {
                    event.getBookmarks().add(new SynchroBookmark(r));
                }
            }else if(lastBookmarkClass == OtherBookmark.class){
                if(s instanceof OtherBookmark){
                    event.getBookmarks().remove(s);
                }else {
                    event.getBookmarks().add(new OtherBookmark(r));
                }
            }

            left.repaint();
        });

        mnuBookmark.addSeparator();

        JRadioButtonMenuItem mCmBookmark = new JRadioButtonMenuItem(LoliSub.RSX.getString("mCmBookmark"));
        mnuBookmark.add(mCmBookmark);
        mCmBookmark.addActionListener((e)->{
            lastBookmarkClass = CommentBookmark.class;
        });

        JRadioButtonMenuItem mTrBookmark = new JRadioButtonMenuItem(LoliSub.RSX.getString("mTrBookmark"));
        mnuBookmark.add(mTrBookmark);
        mTrBookmark.addActionListener((e)->{
            lastBookmarkClass = TransBookmark.class;
        });

        JRadioButtonMenuItem mCkBookmark = new JRadioButtonMenuItem(LoliSub.RSX.getString("mCkBookmark"));
        mnuBookmark.add(mCkBookmark);
        mCkBookmark.addActionListener((e)->{
            lastBookmarkClass = CheckBookmark.class;
        });

        JRadioButtonMenuItem mSyBookmark = new JRadioButtonMenuItem(LoliSub.RSX.getString("mSyBookmark"));
        mnuBookmark.add(mSyBookmark);
        mSyBookmark.addActionListener((e)->{
            lastBookmarkClass = SynchroBookmark.class;
        });

        JRadioButtonMenuItem mOtBookmark = new JRadioButtonMenuItem(LoliSub.RSX.getString("mOtBookmark"));
        mnuBookmark.add(mOtBookmark);
        mOtBookmark.addActionListener((e)->{
            lastBookmarkClass = OtherBookmark.class;
        });

        ButtonGroup bgStripped = new ButtonGroup();
        bgStripped.add(mCmBookmark);
        bgStripped.add(mTrBookmark);
        bgStripped.add(mCkBookmark);
        bgStripped.add(mSyBookmark);
        bgStripped.add(mOtBookmark);
        mCmBookmark.setSelected(true);

        contextMenu.addSeparator();

        JMenuItem mGroupDo = new JMenuItem(LoliSub.RSX.getString("mGroupDo"));
        ImageIcon iGroupDo = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/20px-Crystal_Clear_action_edit_add.png")));
        mGroupDo.setIcon(iGroupDo);
        contextMenu.add(mGroupDo);
        mGroupDo.addActionListener((e) -> {
            int[] indices = table.getAssTable().getSelectedRows();
            int min = indices[0];
            int max = indices[indices.length - 1];
            if(max + 1 >= table.getAssTableModel().getRowCount()) return;
            if(min-1 >= 0){
                AssEvent event = (AssEvent)table.getAssTableModel().getValueAt(
                        min - 1,
                        12
                );
                event.setCollapseElementFollow(true);
            }
            AssEvent event = (AssEvent)table.getAssTableModel().getValueAt(
                    max + 1,
                    12
            );
            event.setCollapseElementPrevious(true);
            List<AssEvent> list = new ArrayList<>();
            for(int i=min; i<max+1; i++){
                list.add((AssEvent)table.getAssTableModel().getValueAt(i, 12));
            }
            for(int i=0; i<list.size(); i++){
                table.getAssTableModel().removeValueAt(min);
            }
            event.setGroup(new CollapseGroup(min, list));
            table.getAssTable().updateUI();
        });

        JMenuItem mGroupUnDo = new JMenuItem(LoliSub.RSX.getString("mGroupUnDo"));
        ImageIcon iGroupUnDo = new ImageIcon(Objects.requireNonNull(MainView.class
                .getResource("/images/20px-Crystal_Clear_action_edit_remove.png")));
        mGroupUnDo.setIcon(iGroupUnDo);
        contextMenu.add(mGroupUnDo);
        mGroupUnDo.addActionListener((e) -> {
            int v = table.getAssTable().getSelectedRow();
            AssEvent event = (AssEvent)table.getAssTableModel().getValueAt(v, 12);
            CollapseGroup cg = event.getGroup();
            if(cg == null) return;
            for(int i=cg.getEvents().size() - 1; i>=0; i--){
                AssEvent x = cg.getEvents().get(i);
                table.getAssTableModel().insertValueAt(x, cg.getFromLine());
            }
            if(cg.getFromLine()-1 >= 0){
                AssEvent x = (AssEvent)table.getAssTableModel().getValueAt(
                        cg.getFromLine() - 1,
                        12
                );
                x.setCollapseElementFollow(false);
            }
            if(cg.getFromLine() + cg.getItemsCount() + 1 < table.getAssTableModel().getRowCount()){
                AssEvent x = (AssEvent)table.getAssTableModel().getValueAt(
                        cg.getFromLine() + cg.getItemsCount(),
                        12
                );
                x.setCollapseElementPrevious(false);
            }
            event.setGroup(null);
            table.getAssTable().updateUI();
        });
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public TablePanel getTable() {
        return table;
    }

    public LeftComponent getLeft() {
        return left;
    }

    public EditorPanel getEditor() {
        return editor;
    }
}