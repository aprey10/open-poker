package com.aprey.jira.plugin.openpoker;

import java.util.List;
import java.util.Objects;

public class SessionViewDTO {
    private final Long moderatorId;
    private final String moderatorName;
    private final SessionStatus status;
    private final List<EstimateDTO> estimates;
    private final List<EstimationGrade> estimationGrades;
    private final List<String> estimatorNameList;

    public SessionViewDTO(Long moderatorId, String moderatorName, SessionStatus status,
                          List<EstimateDTO> estimates,
                          List<EstimationGrade> estimationGrades, List<String> estimatorNameList) {
        this.moderatorId = moderatorId;
        this.moderatorName = moderatorName;
        this.status = status;
        this.estimates = estimates;
        this.estimationGrades = estimationGrades;
        this.estimatorNameList = estimatorNameList;
    }

    public Long getModeratorId() {
        return moderatorId;
    }

    public String getModeratorName() {
        return moderatorName;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public List<EstimateDTO> getEstimates() {
        return estimates;
    }

    public List<EstimationGrade> getEstimationGrades() {
        return estimationGrades;
    }

    public List<String> getEstimatorNameList() {
        return estimatorNameList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        SessionViewDTO that = (SessionViewDTO) o;
        return Objects.equals(moderatorId, that.moderatorId) &&
               Objects.equals(moderatorName, that.moderatorName) && status == that.status &&
               Objects.equals(estimates, that.estimates) &&
               Objects.equals(estimationGrades, that.estimationGrades) &&
               Objects.equals(estimatorNameList, that.estimatorNameList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moderatorName, estimates, status);
    }
}
