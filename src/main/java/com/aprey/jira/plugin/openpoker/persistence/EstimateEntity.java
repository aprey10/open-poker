package com.aprey.jira.plugin.openpoker.persistence;

import net.java.ao.Entity;

public interface EstimateEntity extends Entity {

    PokerSessionEntity getPokerSession();

    void setPokerSession(PokerSessionEntity pokerSession);

    long getEstimatorId();

    void setEstimatorId(long estimatorId);

    int getGradeId();

    void setGradeId(int gradeId);
}
