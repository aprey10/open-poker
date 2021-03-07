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
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Scanned
@Named
public class IssueServiceFacade {

    private static final String STORY_POINT_FILED_NAME = "Story Points";

    @ComponentImport
    private final CustomFieldManager customFieldManager;
    @ComponentImport
    private final IssueService issueService;
    @ComponentImport
    private final IssueManager issueManager;

    @Inject
    public IssueServiceFacade(CustomFieldManager customFieldManager, IssueService issueService,
                              IssueManager issueManager) {
        this.customFieldManager = customFieldManager;
        this.issueService = issueService;
        this.issueManager = issueManager;
    }

    public void applyEstimate(String estimate, ApplicationUser user, String issueId) {
        Issue issue = issueManager.getIssueObject(issueId);
        Optional<CustomField> field = getField(issue);
        if (!field.isPresent()) {
            log.error("'Story Point' custom field does not exist");
            return;
        }
        IssueInputParameters inputParameters = buildInputParams(field.get().getIdAsLong(), estimate);
        IssueService.UpdateValidationResult updateValidationResult = issueService
                .validateUpdate(user, issue.getId(), inputParameters);

        if (!updateValidationResult.isValid()) {
            log.error("Validation for updating story point is failed, errors: {}", updateValidationResult
                    .getErrorCollection().toString());
            return;
        }

        IssueService.IssueResult updateResult = issueService.update(user, updateValidationResult);
        if (!updateResult.isValid()) {
            log.error("ISSUE has NOT been updated. Errors: {}\n" + updateResult.getErrorCollection().toString());
            return;
        }

        log.info("The issue {} has been updated with a new story point value", issueId);
    }

    private IssueInputParameters buildInputParams(long fieldId, String estimate) {
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        issueInputParameters.addCustomFieldValue(fieldId, estimate);
        issueInputParameters.setSkipScreenCheck(true);
        issueInputParameters.setRetainExistingValuesWhenParameterNotProvided(true, true);

        return issueInputParameters;
    }

    private Optional<CustomField> getField(Issue issue) {
        return customFieldManager.getCustomFieldObjects(issue)
                                 .stream()
                                 .filter(f -> f.getFieldName().equals(STORY_POINT_FILED_NAME))
                                 .findFirst();
    }

    public Optional<String> getStoryPoints(String issueId) {
        Issue issue = issueManager.getIssueObject(issueId);
        Optional<Object> value = customFieldManager.getCustomFieldObjects(issue)
                                                   .stream()
                                                   .filter(f -> f.getFieldName().equals(STORY_POINT_FILED_NAME))
                                                   .map(f -> f.getValue(issue))
                                                   .filter(Objects::nonNull)
                                                   .findAny();
        if (!value.isPresent()) {
            return Optional.empty();
        }

        return format(value.get());
    }

    private Optional<String> format(Object value) {
        if (value instanceof Double) {
            DecimalFormat format = new DecimalFormat("0.#");
            return Optional.of(format.format(value));
        }

        if (value instanceof Integer) {
            return Optional.of(value.toString());
        }

        return Optional.empty();
    }
}
