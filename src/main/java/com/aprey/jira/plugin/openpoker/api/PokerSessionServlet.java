package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.google.common.base.Preconditions.checkNotNull;

public class PokerSessionServlet extends HttpServlet {

    private final PersistenceService persistenceService;
    private final ImmutableMap<SessionAction, ActionProcessor> actionProcessorMap;

    @Inject
    public PokerSessionServlet(PersistenceService persistenceService) {
        this.persistenceService = checkNotNull(persistenceService);
        this.actionProcessorMap = ImmutableMap.<SessionAction, ActionProcessor>builder()
                .put(SessionAction.START_SESSION, new StartSessionProcessor())
                .put(SessionAction.STOP_SESSION, new StopSessionProcessor())
                .put(SessionAction.VOTE, new VoteProcessor())
                .put(SessionAction.RE_ESTIMATE, new StartSessionProcessor())
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String issueId = req.getParameter("issueId");
        final SessionAction action = getAction(req.getParameter("action"));

        actionProcessorMap.get(action).process(persistenceService, req, issueId);

        resp.sendRedirect(req.getContextPath() + "/browse/" + issueId);
    }

    private SessionAction getAction(String actionStr) {
        return SessionAction.getAction(actionStr)
                            .orElseThrow(() -> new RuntimeException("Unknown action " + actionStr));
    }
}
