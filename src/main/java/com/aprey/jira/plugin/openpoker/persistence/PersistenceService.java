package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.SessionStatus;
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

@Transactional
@Scanned
@Named
public class PersistenceService {

    @ComponentImport
    private final ActiveObjects ao;
    private final EntityToObjConverter converter;

    @Inject
    public PersistenceService(ActiveObjects ao, EntityToObjConverter entityToObjConverter) {
        this.ao = ao;
        this.converter = entityToObjConverter;
    }

    public void startSession(String issueId, long userId) {
        if (getActiveSessionEntity(issueId).isPresent()) {
            return;
        }
        final PokerSessionEntity session = ao.create(PokerSessionEntity.class);
        session.setIssueId(issueId);
        session.setModeratorId(userId);
        session.setSessionStatus(SessionStatus.IN_PROGRESS);
        session.setUnitOfMeasure(EstimationUnit.FIBONACCI);

        session.save();
    }

    public void addEstimate(String issueId, long estimatorId, int gradeId) {
        final Optional<PokerSessionEntity> sessionOpt = getActiveSessionEntity(issueId);
        if (!sessionOpt.isPresent()) {
            return;
        }
        Optional<EstimateEntity> existingEstimationOpt = findEstimate(estimatorId, sessionOpt.get());
        final EstimateEntity estimate = existingEstimationOpt.orElseGet(() -> ao.create(EstimateEntity.class));

        estimate.setEstimatorId(estimatorId);
        estimate.setGradeId(gradeId);
        estimate.setPokerSession(sessionOpt.get());

        estimate.save();
    }

    public void stopSession(String issueId) {
        final Optional<PokerSessionEntity> sessionOpt = getActiveSessionEntity(issueId);
        if (!sessionOpt.isPresent()) {
            return;
        }
        PokerSessionEntity session = sessionOpt.get();
        session.setSessionStatus(SessionStatus.FINISHED);
        session.setCompletionDate(System.currentTimeMillis());

        session.save();
    }

    private Optional<EstimateEntity> findEstimate(final long estimatorId, final PokerSessionEntity session) {
        EstimateEntity[] estimates = ao.find(EstimateEntity.class,
                                             Query.select()
                                                  .where("ESTIMATOR_ID = ? AND POKER_SESSION_ID = ?",
                                                         estimatorId, session));
        if (estimates.length == 0) {
            return Optional.empty();
        }

        return Optional.of(estimates[0]);
    }

    private Optional<PokerSessionEntity> getActiveSessionEntity(String issueId) {
        return findSessionByIssueIdAndStatus(issueId, SessionStatus.IN_PROGRESS,
                                             sessionList -> Optional.of(sessionList.get(0)));
    }

    public Optional<PokerSession> getActiveSession(String issueId) {
        return getActiveSessionEntity(issueId).map(converter::toObj);
    }

    public Optional<PokerSession> getLatestCompletedSession(String issueId) {
        Function<List<PokerSessionEntity>, Optional<PokerSessionEntity>> theLatestSessionFinder = sessionList ->
                sessionList.stream().sorted(Comparator.comparingLong(PokerSessionEntity::getCompletionDate).reversed())
                           .limit(1)
                           .findAny();

        return findSessionByIssueIdAndStatus(issueId, SessionStatus.FINISHED, theLatestSessionFinder)
                .map(converter::toObj);
    }

    private Optional<PokerSessionEntity> findSessionByIssueIdAndStatus(String issueId, SessionStatus status,
                                                                       Function<List<PokerSessionEntity>, Optional<PokerSessionEntity>> sessionFinder) {
        PokerSessionEntity[] sessions = ao.find(PokerSessionEntity.class, Query.select().where("ISSUE_ID = ? AND " +
                                                                                               "SESSION_STATUS = ?",
                                                                                               issueId,
                                                                                               status));
        if (sessions.length == 0) {
            return Optional.empty();
        }

        return sessionFinder.apply(Arrays.asList(sessions));
    }
}
