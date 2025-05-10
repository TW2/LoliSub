package org.wingate.lolisub.ui.table;

import org.wingate.lolisub.LoliSub;
import org.wingate.lolisub.ass.*;

import javax.swing.table.AbstractTableModel;

public class AssTableModel extends AbstractTableModel {

    private ASS ass;
    private AssStatistics stats;

    public AssTableModel(){
        ass = new ASS();
        stats = new AssStatistics();
    }

    @Override
    public int getRowCount() {
        return ass.getEvents().size();
    }

    @Override
    public int getColumnCount() {
        // 1 Type: Dialogue, Comment
        // 2 Layer
        // 3 Start
        // 4 End
        // 5 Style
        // 6 Actor/Name
        // 7 MarginL
        // 8 MarginR
        // 9 MarginV
        // 10 Effects
        // 11 CPS
        // 12 CPL
        // 13 Text
        return 13;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0 -> { return AssEvent.Type.class; }
            case 1, 6, 7, 8 -> { return Integer.class; }
            case 2, 3 -> { return AssTime.class; }
            case 4 -> { return AssStyle.class; }
            case 5 -> { return AssActor.class; }
            case 9 -> { return AssEffect.class; }
            case 10, 11 -> { return AssStatistics.class; } // Statistics
            case 12 -> { return AssEvent.class; }
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return LoliSub.RSX.getString("tableType"); }
            case 1 -> { return LoliSub.RSX.getString("tableLayer"); }
            case 2 -> { return LoliSub.RSX.getString("tableStart"); }
            case 3 -> { return LoliSub.RSX.getString("tableEnd"); }
            case 4 -> { return LoliSub.RSX.getString("tableStyle"); }
            case 5 -> { return LoliSub.RSX.getString("tableActor"); }
            case 6 -> { return LoliSub.RSX.getString("tableML"); }
            case 7 -> { return LoliSub.RSX.getString("tableMR"); }
            case 8 -> { return LoliSub.RSX.getString("tableMV"); }
            case 9 -> { return LoliSub.RSX.getString("tableFX"); }
            case 10 -> { return LoliSub.RSX.getString("tableCPS"); } // Statistics
            case 11 -> { return LoliSub.RSX.getString("tableCPL"); } // Statistics
            case 12 -> { return LoliSub.RSX.getString("tableText"); }
        }

        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = null;
        AssEvent event = ass.getEvents().get(rowIndex);
        stats.setEvent(event);

        switch(columnIndex){
            case 0 -> { obj = event.getType(); }
            case 1 -> { obj = event.getLayer(); }
            case 2 -> { obj = event.getStart(); }
            case 3 -> { obj = event.getEnd(); }
            case 4 -> { obj = event.getStyle(); }
            case 5 -> { obj = event.getName(); }
            case 6 -> { obj = event.getMarginL(); }
            case 7 -> { obj = event.getMarginR(); }
            case 8 -> { obj = event.getMarginV(); }
            case 9 -> { obj = event.getEffect(); }
            case 10, 11 -> { obj = stats; } // Stats
            case 12 -> { obj = event; }
        }
        return obj;
    }

    @Override
    public void setValueAt(Object v, int rowIndex, int columnIndex) {
        AssEvent event = ass.getEvents().get(rowIndex);
        switch(columnIndex){
            // 0 Line Number
            case 0 -> { if(v instanceof AssEvent.Type x) event.setType(x); }
            case 1 -> { if(v instanceof Integer x) event.setLayer(x); }
            case 2 -> { if(v instanceof AssTime x) event.setStart(x); }
            case 3 -> { if(v instanceof AssTime x) event.setEnd(x); }
            case 4 -> { if(v instanceof AssStyle x) event.setStyle(x); }
            case 5 -> { if(v instanceof AssActor x) event.setName(x); }
            case 6 -> { if(v instanceof Integer x) event.setMarginL(x); }
            case 7 -> { if(v instanceof Integer x) event.setMarginR(x); }
            case 8 -> { if(v instanceof Integer x) event.setMarginV(x); }
            case 9 -> { if(v instanceof AssEffect x) event.setEffect(x); }
            case 10, 11 -> { if(v instanceof AssStatistics x) stats = x; }
            case 12 -> { if(v instanceof AssEvent x) event = x; }
        }
        ass.getEvents().set(rowIndex, event);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addValue(AssEvent event){
        int rowCount = ass.getEvents().size();
        ass.getEvents().add(event);
        fireTableRowsInserted(rowCount, rowCount);
    }

    public void insertValueAt(AssEvent event, int row){
        ass.getEvents().add(row, event);
        fireTableRowsInserted(row, row);
    }

    public void removeValueAt(int row){
        ass.getEvents().remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void replaceValueAt(AssEvent event, int row){
        ass.getEvents().set(row, event);
        fireTableRowsUpdated(row, row);
    }

    //-----------------------------------------------------------------------------

    public ASS getAss() {
        return ass;
    }

    public void setAss(ASS ass) {
        this.ass = ass;
    }

    public AssStatistics getStats() {
        return stats;
    }

    public void setStats(AssStatistics stats) {
        this.stats = stats;
    }
}
