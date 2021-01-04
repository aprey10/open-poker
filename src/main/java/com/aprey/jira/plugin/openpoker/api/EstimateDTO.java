package com.aprey.jira.plugin.openpoker.api;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EstimateDTO {
    private final UserDTO estimator;
    private final String grade;
}
