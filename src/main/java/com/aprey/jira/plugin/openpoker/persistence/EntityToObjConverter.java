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

import com.aprey.jira.plugin.openpoker.Estimate;
import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.UserNotFoundException;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Scanned
class EntityToObjConverter {

    @ComponentImport
    private final UserManager userManager;
    private final EstimationDeckService estimationDeckService;

    @Inject
    public EntityToObjConverter(UserManager userManager, EstimationDeckService estimationDeckService) {
        this.userManager = userManager;
        this.estimationDeckService = estimationDeckService;
    }

    public Estimate toObj(EstimateEntity entity, EstimationUnit estimationUnit) {
        return Estimate.builder()
                       .estimator(getUser(entity.getEstimatorId()))
                       .grade(estimationDeckService.getDeck(estimationUnit).getGrade(entity.getGradeId()).getValue())
                       .gradeId(entity.getGradeId())
                       .build();
    }

    public PokerSession toObj(PokerSessionEntity entity) {
        return PokerSession.builder()
                           .status(entity.getSessionStatus())
                           .issueId(entity.getIssueId())
                           .moderator(getUser(entity.getModeratorId()))
                           .completionDate(entity.getCompletionDate())
                           .estimates(buildEstimates(entity.getEstimates(), entity.getUnitOfMeasure()))
                           .estimationGrades(estimationDeckService.getDeck(entity.getUnitOfMeasure()).getGrades())
                           .build();
    }

    private List<Estimate> buildEstimates(EstimateEntity[] entities, EstimationUnit estimationUnit) {
        return Arrays.stream(entities).map(e -> toObj(e, estimationUnit)).collect(Collectors.toList());
    }

    private ApplicationUser getUser(long userId) {
        return userManager.getUserById(userId).orElseThrow(() -> new UserNotFoundException(
                "User with id " + userId + " is not found"));
    }
}
