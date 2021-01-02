package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import javax.servlet.http.HttpServletRequest;

public class StopSessionProcessor implements ActionProcessor {

    @Override
    public void process(PersistenceService persistenceService, HttpServletRequest request, String issueId) {

        addEstimateIfExist(request, persistenceService, issueId);
        persistenceService.stopSession(issueId);
    }

    private void addEstimateIfExist(HttpServletRequest request, PersistenceService persistenceService, String issueId) {
        final String gradeParam = request.getParameter("estimationGradeId");
        if (gradeParam == null) {
            return;
        }
        final long userId = Long.parseLong(request.getParameter("userId"));
        final int gradeId = Integer.parseInt(gradeParam);

        persistenceService.addEstimate(issueId, userId, gradeId);
    }
}
