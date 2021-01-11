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
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public class StopSessionProcessor implements ActionProcessor {

    @Override
    public void process(PersistenceService persistenceService, HttpServletRequest request, String issueId) {
        addEstimateIfExist(request, persistenceService, issueId);
        persistenceService.stopSession(issueId);
    }

    private void addEstimateIfExist(HttpServletRequest request, PersistenceService persistenceService, String issueId) {
        final Optional<Integer> gradeId = getParam(request, "estimationGradeId");
        if (!gradeId.isPresent()) {
            return;
        }
        final long userId = Long.parseLong(request.getParameter("userId"));

        persistenceService.addEstimate(issueId, userId, gradeId.get());
    }
}
