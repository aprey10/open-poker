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