package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.user.ApplicationUser;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PokerSession {
    private final ApplicationUser moderator;
    private final String issueId;
    private final SessionStatus status;
    private final long completionDate;
    private final List<Estimate> estimates;
}
