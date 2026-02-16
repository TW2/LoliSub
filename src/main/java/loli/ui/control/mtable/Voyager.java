package loli.ui.control.mtable;

import loli.enumeration.ISO_639;
import loli.subtitle.Event;

import java.util.ArrayList;
import java.util.List;

public class Voyager {
    private ISO_639 language;
    private final List<Voyager> voyagers;
    private final List<Bookmark> bookmarks;

    private boolean visible;
    private boolean group;
    private boolean selected;
    private boolean collapsed;
    private String note;
    private Event event;

    public Voyager(ISO_639 language, boolean visible, Event event) {
        voyagers = new ArrayList<>();
        bookmarks = new ArrayList<>();
        this.language = language;
        this.visible = visible;
        this.event = event;
        group = false;
        selected = false;
        collapsed = true;
        note = "";
    }

    public Voyager() {
        this(ISO_639.English, false, null);
    }

    public List<Voyager> getVoyagers() {
        return voyagers;
    }

    public ISO_639 getLanguage() {
        return language;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setLanguage(ISO_639 language) {
        this.language = language;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
