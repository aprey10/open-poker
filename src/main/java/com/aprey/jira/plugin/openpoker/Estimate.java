package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.user.ApplicationUser;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Estimate {
    private final ApplicationUser estimator;
    private final String grade;
}
