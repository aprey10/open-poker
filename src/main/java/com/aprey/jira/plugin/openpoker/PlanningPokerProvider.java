package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.inject.Inject;

import static java.util.stream.Collectors.toMap;

@Scanned
public class PlanningPokerProvider extends AbstractJiraContextProvider {

    private static final Logger log = Logger.getLogger(PlanningPokerProvider.class.getName());
    private static final String ISSUE_ID_KEY = "issue";

    private final PokerSessionService sessionService;

    @ComponentImport
    private final UserManager userManager;

    @Inject
    public PlanningPokerProvider(PokerSessionService sessionService, UserManager userManager) {
        this.sessionService = sessionService;
        this.userManager = userManager;
    }

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        final String issueId = ((Issue) jiraHelper.getContextParams().get(ISSUE_ID_KEY)).getKey();
        final Map<String, Object> contextMap = new HashMap<>();
        Optional<PokerSession> sessionOpt = sessionService.getSession(issueId);
        sessionOpt.ifPresent(pokerSession -> buildSessionView(pokerSession, contextMap));
        contextMap.put("contextPath", jiraHelper.getRequest().getContextPath());
        contextMap.put("issueId", issueId);
        contextMap.put("userId", applicationUser.getId());

        return contextMap;
    }

    private void buildSessionView(PokerSession session, Map<String, Object> contextMap) {
        Map<String, Integer> estimatesMap = Stream.of(session.getEstimates())
                                                  .collect(toMap(
                                                          estimationInfo -> getUsername(
                                                                  estimationInfo.getEstimatorId()),
                                                          EstimationInfo::getEstimationId));

        SessionViewDTO viewDTO = new SessionViewDTO(
                session.getModeratorId(),
                getUsername(session.getModeratorId()),
                estimatesMap,
                session.getSessionStatus(),
                FibonacciNumbers.getValuesList(),
                new ArrayList<>(estimatesMap.keySet()));

        contextMap.put("viewDTO", viewDTO);
    }

    private String getUsername(Long userId) {
        Optional<ApplicationUser> applicationUserOpt = userManager.getUserById(userId);
        if (!applicationUserOpt.isPresent()) {
            log.warning("User is not found for id " + userId);
            return "Unknown";
        }
        return applicationUserOpt.get().getUsername();
    }
}
