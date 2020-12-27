package com.aprey.jira.plugin.openpoker;

import net.java.ao.Entity;
import net.java.ao.OneToMany;

public interface PokerSession extends Entity {

    String getIssueId();

    void setIssueId(String issueId);

    Long getModeratorId();

    void setModeratorId(Long moderatorId);

    SessionStatus getSessionStatus();

    void setSessionStatus(SessionStatus status);

    EstimationUnit getEstimationUnit();

    void setUnitOfMeasure(EstimationUnit estimationUnit);

    @OneToMany
    Estimate[] getEstimates();
}
