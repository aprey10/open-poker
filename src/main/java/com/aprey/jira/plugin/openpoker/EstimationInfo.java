package com.aprey.jira.plugin.openpoker;

import net.java.ao.Entity;

public interface EstimationInfo extends Entity {

    PokerSession getPokerSession();

    void setPokerSession(PokerSession pokerSession);

    long getEstimatorId();

    void setEstimatorId(long estimatorId);

    int getEstimationId();

    void setEstimationId(int estimate);
}
