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

package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import java.util.Collections;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class IssueServiceFacadeTest {

    private static final String ISSUE_ID = "TEST-1";
    private static final int ESTIMATE = 8;
    private static final long FIELD_ID = 1L;
    private static final long ISSUE_ID_LONG = 1L;

    private final Issue issue = mock(Issue.class);
    private final MutableIssue mutableIssue = mock(MutableIssue.class);
    private final ApplicationUser applicationUser = mock(ApplicationUser.class);
    private final IssueInputParameters issueInputParameters = mock(IssueInputParameters.class);
    private final IssueService.UpdateValidationResult updateValidationResult = mock(
            IssueService.UpdateValidationResult.class);

    private final CustomFieldManager customFieldManager = mock(CustomFieldManager.class);
    private final IssueService issueService = mock(IssueService.class);
    private final IssueManager issueManager = mock(IssueManager.class);

    private final IssueServiceFacade issueServiceFacade = new IssueServiceFacade(customFieldManager, issueService,
                                                                                 issueManager);

    @Test
    public void doesNothingIfStoryPointFieldDoesNotExist() {
        when(issueManager.getIssueObject(ISSUE_ID)).thenReturn(mutableIssue);
        when(customFieldManager.getCustomFieldObjects(mutableIssue)).thenReturn(Collections.emptyList());

        issueServiceFacade.applyEstimate(ESTIMATE, applicationUser, ISSUE_ID);

        verifyZeroInteractions(issueService);
    }

    @Test
    @Ignore // Can't mock CustomField cause it fails with 'NoClassDefFound' exception. TODO: find the way to fix it.
    public void doesNothingIfUpdateValidationResultHasErrors() {
        final CustomField customField = mock(CustomField.class);
        when(issueManager.getIssueObject(ISSUE_ID)).thenReturn(mutableIssue);
        when(mutableIssue.getId()).thenReturn(ISSUE_ID_LONG);
        when(customField.getFieldName()).thenReturn("Story Points");
        when(customFieldManager.getCustomFieldObjects(mutableIssue)).thenReturn(Collections.singletonList(customField));
        when(issueService.newIssueInputParameters()).thenReturn(issueInputParameters);
        when(issueService.validateUpdate(applicationUser, ISSUE_ID_LONG, issueInputParameters)).thenReturn(
                updateValidationResult);
        when(updateValidationResult.isValid()).thenReturn(false);

        issueServiceFacade.applyEstimate(ESTIMATE, applicationUser, ISSUE_ID);

        verify(issueInputParameters, times(1)).addCustomFieldValue(FIELD_ID,
                                                                   String.valueOf(ESTIMATE));
        verify(issueInputParameters, times(1)).setSkipScreenCheck(true);
        verify(issueInputParameters, times(1)).setRetainExistingValuesWhenParameterNotProvided(true, true);

        verify(issueService, times(0)).update(applicationUser, updateValidationResult);
    }
}