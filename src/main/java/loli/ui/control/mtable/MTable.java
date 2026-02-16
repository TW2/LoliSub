package loli.ui.control.mtable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MTable extends JPanel {
    private final List<Voyager> voyagers;

    public MTable() {
        voyagers = new ArrayList<>();
    }

    public List<Voyager> getVoyagers() {
        return voyagers;
    }
}
