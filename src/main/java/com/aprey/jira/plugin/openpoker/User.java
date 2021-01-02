package com.aprey.jira.plugin.openpoker;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    private final long id;
    private final String username;
    private final String fullName;
}
