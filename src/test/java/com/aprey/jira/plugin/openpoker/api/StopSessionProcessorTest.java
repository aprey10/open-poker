package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StopSessionProcessorTest {
    private static final String ISSUE_ID = "TEST-1";

    private final PersistenceService persistenceService = mock(PersistenceService.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    private final StopSessionProcessor processor = new StopSessionProcessor();

    @Test
    public void sessionIsSToppedAndNewEstimateIsAdded() {
        when(request.getParameter("estimationGradeId")).thenReturn("1");
        when(request.getParameter("userId")).thenReturn("12");

        processor.process(persistenceService, request, ISSUE_ID);

        verify(persistenceService, times(1)).addEstimate(ISSUE_ID, 12, 1);
        verify(persistenceService, times(1)).stopSession(ISSUE_ID);
    }

    @Test
    public void sessionIsStoppedAndNewEstimateIsNotAddedWhenParamIsNull() {
        when(request.getParameter("estimationGradeId")).thenReturn(null);
        processor.process(persistenceService, request, ISSUE_ID);

        verify(persistenceService, times(1)).stopSession(ISSUE_ID);
        verify(persistenceService, times(0)).addEstimate(eq(ISSUE_ID), anyLong(), anyInt());
    }

    @Test
    public void sessionIsStoppedAndNewEstimateIsNotAddedWhenParamIsEmpty() {
        when(request.getParameter("estimationGradeId")).thenReturn("");
        processor.process(persistenceService, request, ISSUE_ID);

        verify(persistenceService, times(1)).stopSession(ISSUE_ID);
        verify(persistenceService, times(0)).addEstimate(eq(ISSUE_ID), anyLong(), anyInt());
    }

    @Test
    public void sessionIsStoppedAndNewEstimateIsNotAddedWhenParamIsNotInt() {
        when(request.getParameter("estimationGradeId")).thenReturn("test");
        processor.process(persistenceService, request, ISSUE_ID);

        verify(persistenceService, times(1)).stopSession(ISSUE_ID);
        verify(persistenceService, times(0)).addEstimate(eq(ISSUE_ID), anyLong(), anyInt());
    }
}