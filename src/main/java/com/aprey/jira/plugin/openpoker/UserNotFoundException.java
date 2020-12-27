package com.aprey.jira.plugin.openpoker;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4708997251674385306L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
