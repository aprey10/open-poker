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
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import javax.inject.Inject;

@Scanned
public class PlanningPokerWebPanelProvider extends AbstractJiraContextProvider {

    private static final Logger log = Logger.getLogger(PlanningPokerWebPanelProvider.class.getName());
    private static final String ISSUE_ID_KEY = "issue";

    private final PersistenceService sessionService;

    @Inject
    public PlanningPokerWebPanelProvider(PersistenceService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        final String issueId = ((Issue) jiraHelper.getContextParams().get(ISSUE_ID_KEY)).getKey();
        final Map<String, Object> contextMap = new HashMap<>();
        Optional<PokerSession> sessionOpt = findSession(issueId);
        sessionOpt.ifPresent(pokerSession -> buildSessionView(pokerSession, contextMap, applicationUser.getId()));
        contextMap.put("contextPath", jiraHelper.getRequest().getContextPath());
        contextMap.put("issueId", issueId);
        contextMap.put("userId", applicationUser.getId());

        return contextMap;
    }

    private void buildSessionView(PokerSession session, Map<String, Object> contextMap, Long currentUserId) {
        boolean alreadyVoted = (session.getEstimates()
                                       .stream()
                                       .anyMatch(e -> e.getEstimator().getId() == currentUserId));

        SessionViewDTO viewDTO = SessionViewDTO.builder()
                                               .session(session)
                                               .alreadyVoted(alreadyVoted)
                                               .estimationGrades(FibonacciNumber.getValuesList())
                                               .build();

        contextMap.put("viewDTO", viewDTO);
    }

    private Optional<PokerSession> findSession(String issueId) {
        Optional<PokerSession> sessionOpt = sessionService.getActiveSession(issueId);
        if (sessionOpt.isPresent()) {
            return sessionOpt;
        }

        return sessionService.getLatestCompletedSession(issueId);
    }
}
