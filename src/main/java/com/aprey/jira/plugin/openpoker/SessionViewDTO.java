package com.aprey.jira.plugin.openpoker;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SessionViewDTO {
    private final Long moderatorId;
    private final String moderatorName;
    private final Map<String, Integer> estimates;
    private final SessionStatus status;
    private final List<Estimation> points;
    private final List<String> estimatorNameList;

    public SessionViewDTO(Long moderatorId, String moderatorName,
                          Map<String, Integer> estimates, SessionStatus status,
                          List<Estimation> points, List<String> estimatorNameList) {
        this.moderatorId = moderatorId;
        this.moderatorName = moderatorName;
        this.estimates = estimates;
        this.status = status;
        this.points = points;
        this.estimatorNameList = estimatorNameList;
    }

    public Long getModeratorId() {
        return moderatorId;
    }

    public String getModeratorName() {
        return moderatorName;
    }

    public Map<String, Integer> getEstimates() {
        return estimates;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public List<Estimation> getPoints() {
        return points;
    }

    public List<String> getEstimatorNameList() {
        return estimatorNameList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        SessionViewDTO that = (SessionViewDTO) o;
        return moderatorName.equals(that.moderatorName) &&
               Objects.equals(estimates, that.estimates) &&
               status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moderatorName, estimates, status);
    }
}
