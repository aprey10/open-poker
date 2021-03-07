/*
 * Copyright (C) 2021  Andriy Preizner
 *
 * This file is a part of Open Poker jira plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationGrade;
import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.IssueServiceFacade;
import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.SessionStatus;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Scanned
@Named
@Slf4j
public class PersistenceService {

    @ComponentImport
    private final ActiveObjects ao;
    private final EntityToObjConverter converter;
    private final QueryBuilderService queryBuilder;
    private final IssueServiceFacade issueServiceFacade;
    private final EstimationDeckService deckService;

    @Inject
    public PersistenceService(ActiveObjects ao, EntityToObjConverter converter,
                              QueryBuilderService queryBuilderService,
                              IssueServiceFacade issueServiceFacade,
                              EstimationDeckService estimationDeckService) {
        this.ao = ao;
        this.converter = converter;
        this.queryBuilder = queryBuilderService;
        this.issueServiceFacade = issueServiceFacade;
        this.deckService = estimationDeckService;
    }

    public void startSession(String issueId, long userId, EstimationUnit estimationUnit) {
        if (getActiveSessionEntity(issueId).isPresent()) {
            return;
        }
        final PokerSessionEntity session = ao.create(PokerSessionEntity.class);
        session.setIssueId(issueId);
        session.setModeratorId(userId);
        session.setSessionStatus(SessionStatus.IN_PROGRESS);
        session.setUnitOfMeasure(estimationUnit);

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

        if (session.getEstimates().length == 0) {
            deleteSessions(issueId);
            return;
        }

        session.setSessionStatus(SessionStatus.FINISHED);
        session.setCompletionDate(System.currentTimeMillis());

        session.save();
    }

    public Optional<String> getFinaleEstimate(String issueId) {
        return findFinalEstimateEntity(issueId).map(e -> Optional.ofNullable(e.getEstimateValue()))
                                               .orElse(issueServiceFacade.getStoryPoints(issueId));
    }

    public void applyFinalEstimate(String issueId, int estimateId, ApplicationUser applicationUser) {
        List<PokerSessionEntity> orderedSessions = getAllSessions(issueId);
        if (orderedSessions.isEmpty()) {
            log.error("No completed sessions for issue with id {}", issueId);
            return;
        }

        PokerSessionEntity latestSession = orderedSessions.get(0);
        EstimationGrade estimationGrade = getEstimationGrade(estimateId, latestSession.getUnitOfMeasure());

        if (estimationGrade.isApplicable()) {
            issueServiceFacade.applyEstimate(estimationGrade.getValue(), applicationUser, issueId);
        }

        saveFinalEstimate(estimationGrade.getValue(), issueId);
        deleteSessions(orderedSessions);
    }

    private EstimationGrade getEstimationGrade(int estimationId, EstimationUnit estimationUnit) {
        return deckService.getDeck(estimationUnit).getGrade(estimationId);
    }

    private void saveFinalEstimate(String estimate, String issueId) {
        FinalEstEntity estimateEntity = getFinalEstimateEntity(issueId);
        estimateEntity.setEstimateValue(estimate);
        estimateEntity.setIssueId(issueId);

        estimateEntity.save();
    }

    private Optional<FinalEstEntity> findFinalEstimateEntity(String issueId) {
        FinalEstEntity[] estimates = ao.find(FinalEstEntity.class, queryBuilder.whereIssuerId(issueId));
        if (estimates.length == 0) {
            return Optional.empty();
        }

        return Optional.of(estimates[0]);
    }

    private FinalEstEntity getFinalEstimateEntity(String issueId) {
        return findFinalEstimateEntity(issueId).orElse(ao.create(FinalEstEntity.class));
    }

    private void deleteSessionAndEstimates(PokerSessionEntity session) {
        if (session.getEstimates() != null) {
            Arrays.stream(session.getEstimates()).forEach(ao::delete);
        }

        ao.delete(session);
    }

    public void deleteSessions(String issueId) {
        PokerSessionEntity[] sessions = ao.find(PokerSessionEntity.class,
                                                queryBuilder.whereIssuerId(issueId));
        if (sessions == null) {
            return;
        }

        deleteSessions(Arrays.stream(sessions).collect(Collectors.toList()));
    }

    private void deleteSessions(List<PokerSessionEntity> sessions) {
        sessions.forEach(this::deleteSessionAndEstimates);
    }

    private Optional<EstimateEntity> findEstimate(final long estimatorId, final PokerSessionEntity session) {
        EstimateEntity[] estimates = ao.find(EstimateEntity.class,
                                             queryBuilder.estimateWhereEstimatorIdAndSessionId(estimatorId, session));
        if (estimates.length == 0) {
            return Optional.empty();
        }

        return Optional.of(estimates[0]);
    }

    private Optional<PokerSessionEntity> getActiveSessionEntity(String issueId) {
        PokerSessionEntity[] sessions = ao.find(PokerSessionEntity.class,
                                                queryBuilder.sessionWhereIssueIdAndStatus(issueId,
                                                                                          SessionStatus.IN_PROGRESS));
        if (sessions.length == 0) {
            return Optional.empty();
        }

        return Optional.of(sessions[0]);
    }

    public Optional<PokerSession> getActiveSession(String issueId) {
        return getActiveSessionEntity(issueId).map(converter::toObj);
    }

    public Optional<PokerSession> getLatestCompletedSession(String issueId) {
        Function<List<PokerSessionEntity>, Optional<PokerSessionEntity>> theLatestSessionFinder = sessionList ->
                reverseOrderSessions(sessionList.stream())
                        .limit(1)
                        .findAny();

        return findSessionByIssueIdAndStatus(issueId, SessionStatus.FINISHED, theLatestSessionFinder).map(
                converter::toObj);
    }

    private List<PokerSessionEntity> getAllSessions(String issueId) {
        PokerSessionEntity[] sessions = ao.find(PokerSessionEntity.class,
                                                queryBuilder.whereIssuerId(issueId));

        return reverseOrderSessions(Arrays.stream(sessions)).collect(Collectors.toList());
    }

    private Stream<PokerSessionEntity> reverseOrderSessions(Stream<PokerSessionEntity> sessions) {
        return sessions.sorted(Comparator.comparingLong(PokerSessionEntity::getCompletionDate).reversed());
    }

    private Optional<PokerSessionEntity> findSessionByIssueIdAndStatus(String issueId, SessionStatus status,
                                                                       Function<List<PokerSessionEntity>,
                                                                               Optional<PokerSessionEntity>> sessionFinder) {
        PokerSessionEntity[] sessions = ao.find(PokerSessionEntity.class,
                                                queryBuilder.sessionWhereIssueIdAndStatus(issueId, status));

        if (sessions.length == 0) {
            return Optional.empty();
        }

        return sessionFinder.apply(Arrays.asList(sessions));
    }
}
