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
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Named
@Scanned
@Slf4j
public class ApplyVoteProcessor implements ActionProcessor {

    private static final String ESTIMATE_FILED = "finalEstimateId";
    private static final String USER_ID_FIELD = "userId";

    @ComponentImport
    private final UserManager userManager;

    @Inject
    public ApplyVoteProcessor(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void process(PersistenceService persistenceService, HttpServletRequest request, String issueId) {
        final long userId = Long.parseLong(request.getParameter(USER_ID_FIELD));
        final int estimateId = Integer.parseInt(request.getParameter(ESTIMATE_FILED));
        Optional<ApplicationUser> applicationUser = userManager.getUserById(userId);
        if (!applicationUser.isPresent()) {
            log.error("Application user is not found by {} id", userId);
            return;
        }

        persistenceService.applyFinalEstimate(issueId, estimateId, applicationUser.get());
    }
}
