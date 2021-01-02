package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.SessionStatus;
import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import org.junit.Test;

import static org.mockito.Matchers.any;
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
    private final EntityToObjConverter converter = mock(EntityToObjConverter.class);

    private final PersistenceService service = new PersistenceService(ao, converter);

    @Test
    public void sessionIsStarted() {
        when(ao.find(eq(PokerSessionEntity.class), any(Query.class))).thenReturn(new PokerSessionEntity[]{});
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
        when(ao.find(eq(PokerSessionEntity.class), any(Query.class))).thenReturn(
                new PokerSessionEntity[]{sessionEntity});

        service.startSession(ISSUE_ID, USER_ID);

        verifyZeroInteractions(sessionEntity);
        verify(ao, times(0)).create(eq(PokerSessionEntity.class));
    }
}