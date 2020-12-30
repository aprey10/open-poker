package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;

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
        Optional<PokerSession> sessionOpt = findSession(issueId);
        sessionOpt.ifPresent(pokerSession -> buildSessionView(pokerSession, contextMap, applicationUser.getId()));
        contextMap.put("contextPath", jiraHelper.getRequest().getContextPath());
        contextMap.put("issueId", issueId);
        contextMap.put("userId", applicationUser.getId());

        return contextMap;
    }

    private void buildSessionView(PokerSession session, Map<String, Object> contextMap, Long currentUserId) {
        List<EstimateDTO> estimates = Arrays.stream(session.getEstimates())
                                            .map(this::buildEstimateDTO)
                                            .collect(Collectors.toList());
        boolean alreadyVoted = Arrays.stream(session.getEstimates()).anyMatch(e -> e.getEstimatorId() == currentUserId);

        SessionViewDTO viewDTO = new SessionViewDTO(
                session.getModeratorId(),
                getUsername(session.getModeratorId()),
                session.getSessionStatus(),
                estimates,
                FibonacciNumber.getValuesList(),
                alreadyVoted
        );

        contextMap.put("viewDTO", viewDTO);
    }

    private Optional<PokerSession> findSession(String issueId) {
        Optional<PokerSession> sessionOpt = sessionService.getActiveSession(issueId);
        if (sessionOpt.isPresent()) {
            return sessionOpt;
        }

        return sessionService.getLatestCompletedSession(issueId);
    }

    private EstimateDTO buildEstimateDTO(Estimate estimate) {
        final String estimateGrade = FibonacciNumber.findById(estimate.getGradeId()).getValue();
        final ApplicationUser estimator = getUser(estimate.getEstimatorId());

        return new EstimateDTO(estimateGrade, estimator.getDisplayName(), estimator.getUsername());
    }

    private String getUsername(Long userId) {
        Optional<ApplicationUser> applicationUserOpt = userManager.getUserById(userId);
        if (!applicationUserOpt.isPresent()) {
            log.warning("User is not found for id " + userId);
            return "Unknown";
        }
        return applicationUserOpt.get().getUsername();
    }

    private ApplicationUser getUser(Long userId) {
        return userManager.getUserById(userId).orElseThrow(() -> new UserNotFoundException(
                "User with id " + userId + " is not found"));
    }
}
