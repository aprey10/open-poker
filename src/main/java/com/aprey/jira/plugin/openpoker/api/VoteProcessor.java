package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

//TODO: cover with unit tests
public class VoteProcessor implements ActionProcessor {

    @Override
    public void process(PersistenceService persistenceService, HttpServletRequest request, String issueId) {
        final long userId = Long.parseLong(request.getParameter("userId"));

        final Optional<Integer> gradeId = getParam(request, "estimationGradeId");
        if (!gradeId.isPresent()) {
            return;
        }

        persistenceService.addEstimate(issueId, userId, gradeId.get());
    }
}
