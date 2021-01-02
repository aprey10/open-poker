package com.aprey.jira.plugin.openpoker;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PokerSession {
    private final User moderator;
    private final String issueId;
    private final SessionStatus status;
    private final long completionDate;
    private final List<Estimate> estimates;
}
