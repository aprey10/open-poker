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

import com.aprey.jira.plugin.openpoker.Estimate;
import com.aprey.jira.plugin.openpoker.FibonacciNumber;
import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;

@Scanned
public class PlanningPokerWebPanelProvider extends AbstractJiraContextProvider {

    private static final Logger log = Logger.getLogger(PlanningPokerWebPanelProvider.class.getName());
    private static final String ISSUE_ID_KEY = "issue";

    private final PersistenceService sessionService;
    private final UserConverter userConverter;

    @Inject
    public PlanningPokerWebPanelProvider(PersistenceService sessionService,
                                         UserConverter userConverter) {
        this.sessionService = sessionService;
        this.userConverter = userConverter;
    }

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        final String issueId = ((Issue) jiraHelper.getContextParams().get(ISSUE_ID_KEY)).getKey();
        final Map<String, Object> contextMap = new HashMap<>();
        Optional<PokerSession> sessionOpt = findSession(issueId);
        sessionOpt.ifPresent(pokerSession -> buildSessionView(pokerSession, contextMap, applicationUser));
        contextMap.put("contextPath", jiraHelper.getRequest().getContextPath());
        contextMap.put("issueId", issueId);
        contextMap.put("userId", applicationUser.getId());

        return contextMap;
    }

    private void buildSessionView(PokerSession session, Map<String, Object> contextMap, ApplicationUser currentUser) {
        boolean alreadyVoted = (session.getEstimates()
                                       .stream()
                                       .anyMatch(e -> e.getEstimator().equals(currentUser)));

        List<EstimateDTO> estimates = session.getEstimates()
                                             .stream()
                                             .map(e -> toEstimateDto(currentUser, e))
                                             .collect(Collectors.toList());

        SessionViewDTO viewDTO = SessionViewDTO.builder()
                                               .session(session)
                                               .alreadyVoted(alreadyVoted)
                                               .estimationGrades(FibonacciNumber.getValuesList())
                                               .estimates(estimates)
                                               .moderator(
                                                       userConverter.buildUserDto(session.getModerator(), currentUser))
                                               .build();

        contextMap.put("viewDTO", viewDTO);
    }

    private EstimateDTO toEstimateDto(ApplicationUser currentUser, Estimate estimate) {
        return EstimateDTO.builder()
                          .grade(estimate.getGrade())
                          .estimator(userConverter.buildUserDto(estimate.getEstimator(), currentUser))
                          .build();
    }

    private Optional<PokerSession> findSession(String issueId) {
        Optional<PokerSession> sessionOpt = sessionService.getActiveSession(issueId);
        if (sessionOpt.isPresent()) {
            return sessionOpt;
        }

        return sessionService.getLatestCompletedSession(issueId);
    }
}
