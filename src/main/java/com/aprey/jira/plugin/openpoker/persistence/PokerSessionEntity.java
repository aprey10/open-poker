package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.SessionStatus;
import net.java.ao.Entity;
import net.java.ao.OneToMany;

public interface PokerSessionEntity extends Entity {

    String getIssueId();

    void setIssueId(String issueId);

    Long getModeratorId();

    void setModeratorId(Long moderatorId);

    SessionStatus getSessionStatus();

    void setSessionStatus(SessionStatus status);

    EstimationUnit getEstimationUnit();

    void setUnitOfMeasure(EstimationUnit estimationUnit);

    void setCompletionDate(long timestamp);

    long getCompletionDate();

    @OneToMany
    EstimateEntity[] getEstimates();
}
