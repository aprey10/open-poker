package com.aprey.jira.plugin.openpoker;

public class EstimateDTO {
    private final String estimate;
    private final String estimatorDisplayName;
    private final String estimatorUsername;

    public EstimateDTO(String estimate, String estimatorDisplayName, String estimatorUsername) {
        this.estimate = estimate;
        this.estimatorDisplayName = estimatorDisplayName;
        this.estimatorUsername = estimatorUsername;
    }

    public String getEstimate() {
        return estimate;
    }

    public String getEstimatorDisplayName() {
        return estimatorDisplayName;
    }

    public String getEstimatorUsername() {
        return estimatorUsername;
    }
}
