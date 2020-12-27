package com.aprey.jira.plugin.openpoker;

import net.java.ao.Entity;

public interface Estimate extends Entity {

    PokerSession getPokerSession();

    void setPokerSession(PokerSession pokerSession);

    long getEstimatorId();

    void setEstimatorId(long estimatorId);

    int getGradeId();

    void setGradeId(int gradeId);
}
