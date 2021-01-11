package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.SessionStatus;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import javax.inject.Named;
import net.java.ao.Query;

@Scanned
@Named
public class QueryBuilderService {

    Query sessionWhereIssueIdAndStatus(String issueId, SessionStatus status) {
        return Query.select().where("ISSUE_ID = ? AND SESSION_STATUS = ?", issueId, status);
    }

    Query estimateWhereEstimatorIdAndSessionId(Long estimatorId, PokerSessionEntity session) {
        return Query.select().where("POKER_SESSION_ID = ? AND ESTIMATOR_ID = ?", session, estimatorId);
    }

    Query sessionWhereIssuerId(String issueId) {
        return Query.select().where("ISSUE_ID = ?", issueId);
    }
}
