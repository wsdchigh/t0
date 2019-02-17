package com.wsdc.g_a_0.annotation;

public enum EWrapType {
    ACTIVITY("activity"),FRAGMENT("fragment"),VIEW("view"),
    SERVICE("service"),BROADCAST("broadcast"),DIALOG("dialog"),POP_WINDOW("pop_window");

    public String name;

    EWrapType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
