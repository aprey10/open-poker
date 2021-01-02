package com.aprey.jira.plugin.openpoker.api;

import java.util.Optional;
import java.util.stream.Stream;

public enum SessionAction {
    START_SESSION("Start Estimation Session"),
    VOTE("Vote"),
    STOP_SESSION("Stop Session"),
    RE_ESTIMATE("Re-estimate");

    private final String actionName;

    private SessionAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public static Optional<SessionAction> getAction(String actionName) {
        return Stream.of(SessionAction.values())
                     .filter(a -> a.getActionName().equals(actionName))
                     .findAny();
    }
}
