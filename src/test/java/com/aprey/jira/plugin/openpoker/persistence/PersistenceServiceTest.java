package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.SessionStatus;
import com.atlassian.activeobjects.external.ActiveObjects;
import java.util.Optional;
import net.java.ao.Query;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PersistenceServiceTest {

    private static final String ISSUE_ID = "ID";
    private static final long USER_ID = 1;
    private static final long ESTIMATOR_ID = 2;
    private static final int GRADE_ID = 10;

    private final PokerSessionEntity sessionEntity = mock(PokerSessionEntity.class);
    private final EstimateEntity estimateEntity = mock(EstimateEntity.class);
    private final ActiveObjects ao = mock(ActiveObjects.class);
    private final QueryBuilderService queryBuilder = mock(QueryBuilderService.class);
    private final Query sessionQuery = mock(Query.class);
    private final Query estimateQuery = mock(Query.class);
    private final EntityToObjConverter converter = mock(EntityToObjConverter.class);

    private final PersistenceService service = new PersistenceService(ao, converter, queryBuilder);

    @Test
    public void sessionIsStarted() {
        when(queryBuilder.sessionWhereIssueIdAndStatus(ISSUE_ID, SessionStatus.IN_PROGRESS)).thenReturn(sessionQuery);
        when(ao.find(eq(PokerSessionEntity.class), eq(sessionQuery))).thenReturn(new PokerSessionEntity[]{});
        when(ao.create(eq(PokerSessionEntity.class))).thenReturn(sessionEntity);

        service.startSession(ISSUE_ID, USER_ID);

        verify(sessionEntity, times(1)).setIssueId(eq(ISSUE_ID));
        verify(sessionEntity, times(1)).setModeratorId(eq(USER_ID));
        verify(sessionEntity, times(1)).setSessionStatus(eq(SessionStatus.IN_PROGRESS));
        verify(sessionEntity, times(1)).setUnitOfMeasure(eq(EstimationUnit.FIBONACCI));
        verify(sessionEntity, times(1)).save();
    }

    @Test
    public void sessionIsAlreadyStarted() {
        when(queryBuilder.sessionWhereIssueIdAndStatus(ISSUE_ID, SessionStatus.IN_PROGRESS)).thenReturn(sessionQuery);
        when(ao.find(eq(PokerSessionEntity.class), eq(sessionQuery))).thenReturn(
                new PokerSessionEntity[]{sessionEntity});

        service.startSession(ISSUE_ID, USER_ID);

        verifyZeroInteractions(sessionEntity);
        verify(ao, times(0)).create(eq(PokerSessionEntity.class));
    }

    @Test
    public void newEstimateIsAdded() {
        //        TODO: replace any(Query.class) with concrete equal
        when(ao.find(eq(PokerSessionEntity.class), any(Query.class))).thenReturn(
                new PokerSessionEntity[]{sessionEntity});

        when(ao.find(eq(EstimateEntity.class), any(Query.class))).thenReturn(new EstimateEntity[]{});
        when(ao.create(EstimateEntity.class)).thenReturn(estimateEntity);

        service.addEstimate("TEST-1", 12L, 12);

        verify(estimateEntity, times(1)).setEstimatorId(12L);
        verify(estimateEntity, times(1)).setGradeId(12);
        verify(estimateEntity, times(1)).setPokerSession(sessionEntity);
        verify(estimateEntity, times(1)).save();
    }

    @Test
    public void existingEstimateIsUpdated() {
        //        TODO: replace any(Query.class) with concrete equal
        when(ao.find(eq(PokerSessionEntity.class), any(Query.class))).thenReturn(
                new PokerSessionEntity[]{sessionEntity});

        when(ao.find(eq(EstimateEntity.class), any(Query.class))).thenReturn(new EstimateEntity[]{estimateEntity});

        service.addEstimate("TEST-1", 12L, 12);

        verify(ao, times(0)).create(eq(EstimateEntity.class));
        verify(estimateEntity, times(1)).setEstimatorId(12L);
        verify(estimateEntity, times(1)).setGradeId(12);
        verify(estimateEntity, times(1)).setPokerSession(sessionEntity);
        verify(estimateEntity, times(1)).save();
    }

    @Test
    public void sessionIsStopped() {
        when(queryBuilder.sessionWhereIssueIdAndStatus(ISSUE_ID, SessionStatus.IN_PROGRESS)).thenReturn(sessionQuery);
        when(ao.find(eq(PokerSessionEntity.class), eq(sessionQuery))).thenReturn(
                new PokerSessionEntity[]{sessionEntity});

        service.stopSession(ISSUE_ID);

        verify(sessionEntity, times(1)).setSessionStatus(SessionStatus.FINISHED);
        verify(sessionEntity, times(1)).setCompletionDate(anyLong());
        verify(sessionEntity, times(1)).save();
    }

    @Test
    public void doesNothingIfThereIsNoSessionInProgress() {
        when(queryBuilder.sessionWhereIssueIdAndStatus(ISSUE_ID, SessionStatus.IN_PROGRESS)).thenReturn(sessionQuery);
        when(ao.find(eq(PokerSessionEntity.class), eq(sessionQuery))).thenReturn(
                new PokerSessionEntity[]{});

        service.stopSession(ISSUE_ID);
        verifyZeroInteractions(sessionEntity);
    }

    @Test
    public void findsActiveSession() {
        PokerSession pokerSession = PokerSession.builder().build();
        when(queryBuilder.sessionWhereIssueIdAndStatus(ISSUE_ID, SessionStatus.IN_PROGRESS)).thenReturn(sessionQuery);
        when(ao.find(eq(PokerSessionEntity.class), eq(sessionQuery))).thenReturn(
                new PokerSessionEntity[]{sessionEntity});
        when(converter.toObj(sessionEntity)).thenReturn(pokerSession);

        Optional<PokerSession> actual = service.getActiveSession(ISSUE_ID);

        assertTrue(actual.isPresent());
        assertEquals(pokerSession, actual.get());
    }
}