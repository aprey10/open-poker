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
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ApplyVoteProcessorTest {

    private static final String ISSUE_ID = "TEST-1";
    private static final long USER_ID = 1;
    private static final int ESTIMATE_ID = 2;

    private final PersistenceService persistenceService = mock(PersistenceService.class);
    private final UserManager userManager = mock(UserManager.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final ApplicationUser applicationUser = mock(ApplicationUser.class);

    private final ApplyVoteProcessor applyVoteProcessor = new ApplyVoteProcessor(userManager);

    @Test
    public void estimateIsAppliedAndSessionIsDeleted() {
        when(request.getParameter("userId")).thenReturn(String.valueOf(USER_ID));
        when(request.getParameter("finalEstimateId")).thenReturn(String.valueOf(ESTIMATE_ID));
        when(userManager.getUserById(USER_ID)).thenReturn(Optional.of(applicationUser));

        applyVoteProcessor.process(persistenceService, request, ISSUE_ID);

        verify(persistenceService, times(1)).applyFinalEstimate(ISSUE_ID, ESTIMATE_ID,
                                                                applicationUser);
    }

    @Test
    public void estimateIsNotAppliedIfUserIsNotFound() {
        when(request.getParameter("userId")).thenReturn(String.valueOf(USER_ID));
        when(request.getParameter("finalEstimateId")).thenReturn(String.valueOf(ESTIMATE_ID));
        when(userManager.getUserById(USER_ID)).thenReturn(Optional.empty());

        applyVoteProcessor.process(persistenceService, request, ISSUE_ID);

        verifyZeroInteractions(persistenceService);
    }
}