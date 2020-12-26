package com.aprey.jira.plugin.openpoker;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import net.java.ao.Query;

import static java.lang.String.format;

@Transactional
@Scanned
@Named
public class PokerSessionService {

    private static final String SESSION_NOT_FOUND_EXP = "Issue with id %s doesn't have related estimation session";

    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public PokerSessionService(ActiveObjects ao) {
        this.ao = ao;
    }

    public PokerSession startSession(String issueId, long userId) {
        final PokerSession session = ao.create(PokerSession.class);
        session.setIssueId(issueId);
        session.setModeratorId(userId);
        session.setSessionStatus(SessionStatus.IN_PROGRESS);
        session.setUnitOfMeasure(EstimationUnit.FIBONACCI);

        session.save();
        return session;
    }

    public void addEstimate(String issueId, long estimatorId, int estimationId) {
        final PokerSession session = getSession(issueId).orElseThrow(
                () -> new SessionNotFoundException(format(SESSION_NOT_FOUND_EXP, issueId)));
        Optional<EstimationInfo> existingEstimationOpt = findEstimation(estimatorId, session);
        final EstimationInfo info = existingEstimationOpt.orElseGet(() -> ao.create(EstimationInfo.class));

        info.setEstimatorId(estimatorId);
        info.setEstimationId(estimationId);
        info.setPokerSession(session);

        info.save();
    }

    private Optional<EstimationInfo> findEstimation(final long estimatorId, final PokerSession session) {
        EstimationInfo[] estimationInfos = ao.find(EstimationInfo.class,
                                                   Query.select()
                                                        .where("ESTIMATOR_ID = ? AND POKER_SESSION_ID = ?",
                                                               estimatorId, session));
        if (estimationInfos.length == 0) {
            return Optional.empty();
        }

        return Optional.of(estimationInfos[0]);
    }

    public void stopSession(String issueId, long userId) {
        final Optional<PokerSession> sessionOpt = getSession(issueId);
        if (!sessionOpt.isPresent()) {
            throw new SessionNotFoundException(format(SESSION_NOT_FOUND_EXP, issueId));
        }
        PokerSession session = sessionOpt.get();
        session.setSessionStatus(SessionStatus.FINISHED);

        session.save();
    }

    public Optional<PokerSession> getSession(String issueId) {
        PokerSession[] sessions = ao.find(PokerSession.class, Query.select().where("ISSUE_ID = ?", issueId));
        if (sessions.length == 0) {
            return Optional.empty();
        }
        return Optional.of(sessions[0]);
    }
}
