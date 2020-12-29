package com.aprey.jira.plugin.openpoker;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

    public void addEstimate(String issueId, long estimatorId, int gradeId) {
        final PokerSession session = getActiveSession(issueId).orElseThrow(
                () -> new SessionNotFoundException(format(SESSION_NOT_FOUND_EXP, issueId)));
        Optional<Estimate> existingEstimationOpt = findEstimation(estimatorId, session);
        final Estimate info = existingEstimationOpt.orElseGet(() -> ao.create(Estimate.class));

        info.setEstimatorId(estimatorId);
        info.setGradeId(gradeId);
        info.setPokerSession(session);

        info.save();
    }

    private Optional<Estimate> findEstimation(final long estimatorId, final PokerSession session) {
        Estimate[] estimates = ao.find(Estimate.class,
                                       Query.select()
                                            .where("ESTIMATOR_ID = ? AND POKER_SESSION_ID = ?",
                                                   estimatorId, session));
        if (estimates.length == 0) {
            return Optional.empty();
        }

        return Optional.of(estimates[0]);
    }

    public void stopSession(String issueId) {
        final Optional<PokerSession> sessionOpt = getActiveSession(issueId);
        if (!sessionOpt.isPresent()) {
            throw new SessionNotFoundException(format(SESSION_NOT_FOUND_EXP, issueId));
        }
        PokerSession session = sessionOpt.get();
        session.setSessionStatus(SessionStatus.FINISHED);
        session.setCompletionDate(System.currentTimeMillis());

        session.save();
    }

    public Optional<PokerSession> getActiveSession(String issueId) {
        return findSessionByIssueIdAndStatus(issueId, SessionStatus.IN_PROGRESS,
                                             sessionList -> Optional.of(sessionList.get(0)));
    }

    public Optional<PokerSession> getLatestCompletedSession(String issueId) {
        Function<List<PokerSession>, Optional<PokerSession>> theLatestSessionFinder = sessionList ->
                sessionList.stream().sorted(Comparator.comparingLong(PokerSession::getCompletionDate).reversed())
                           .limit(1)
                           .findAny();

        return findSessionByIssueIdAndStatus(issueId, SessionStatus.FINISHED, theLatestSessionFinder);
    }

    private Optional<PokerSession> findSessionByIssueIdAndStatus(String issueId, SessionStatus status,
                                                                 Function<List<PokerSession>, Optional<PokerSession>> sessionFinder) {
        PokerSession[] sessions = ao.find(PokerSession.class, Query.select().where("ISSUE_ID = ? AND " +
                                                                                   "SESSION_STATUS = ?", issueId,
                                                                                   status));
        if (sessions.length == 0) {
            return Optional.empty();
        }

        return sessionFinder.apply(Arrays.asList(sessions));
    }
}
