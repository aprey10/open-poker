package com.aprey.jira.plugin.openpoker.persistence;

import net.java.ao.Entity;
import net.java.ao.schema.Index;
import net.java.ao.schema.Indexes;

@Indexes(@Index(name = "pokerSession", methodNames = {"getPokerSession"}))
public interface EstimateEntity extends Entity {

    PokerSessionEntity getPokerSession();

    void setPokerSession(PokerSessionEntity pokerSession);

    long getEstimatorId();

    void setEstimatorId(long estimatorId);

    int getGradeId();

    void setGradeId(int gradeId);
}
