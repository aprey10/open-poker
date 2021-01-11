package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import javax.servlet.http.HttpServletRequest;

public class DeleteSessionProcessor implements ActionProcessor {

    @Override
    public void process(PersistenceService persistenceService, HttpServletRequest request, String issueId) {
        persistenceService.deleteSessions(issueId);
    }
}
