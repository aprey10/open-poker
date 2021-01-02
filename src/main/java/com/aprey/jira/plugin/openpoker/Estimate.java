package com.aprey.jira.plugin.openpoker;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Estimate {
    private final User estimator;
    private final String grade;
}
