package loli.enumeration;

import loli.Sub;

public enum AssEventType {
    Comment(Sub.L.getString("event.type.comment")),
    Dialogue(Sub.L.getString("event.type.dialogue")),
    Tagged(Sub.L.getString("event.type.tagged"));

    final String name;

    AssEventType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
