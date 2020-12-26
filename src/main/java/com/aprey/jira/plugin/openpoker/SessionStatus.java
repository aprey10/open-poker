package com.aprey.jira.plugin.openpoker;

public enum SessionStatus {
    IN_PROGRESS("In Progress"),
    FINISHED("Finished");

    SessionStatus(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
