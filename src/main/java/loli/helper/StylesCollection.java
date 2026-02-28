package loli.helper;

import java.util.ArrayList;
import java.util.List;

public class StylesCollection {
    private String name;
    private final List<AssStyle> styles;

    public StylesCollection(String name) {
        this.name = name;
        styles = new ArrayList<>();
    }

    public List<AssStyle> getStyles() {
        return styles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
