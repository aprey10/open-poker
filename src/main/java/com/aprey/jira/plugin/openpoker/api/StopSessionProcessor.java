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
