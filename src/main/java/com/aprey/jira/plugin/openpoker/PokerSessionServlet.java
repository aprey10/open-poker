package com.aprey.jira.plugin.openpoker;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.google.common.base.Preconditions.checkNotNull;

public class PokerSessionServlet extends HttpServlet {

    private final PokerSessionService sessionService;

    public PokerSessionServlet(PokerSessionService sessionService) {
        this.sessionService = checkNotNull(sessionService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String issueId = req.getParameter("issueId");
        final long userId = Long.parseLong(req.getParameter("userId"));
        final SessionAction action = getAction(req.getParameter("action"));
        switch (action) {
            case START_SESSION:
                sessionService.startSession(issueId, userId);
                break;
            case VOTE:
                final int gradeId = Integer.parseInt(req.getParameter("estimationGradeId"));
                sessionService.addEstimate(issueId, userId, gradeId);
                break;
            case STOP_SESSION:
                sessionService.stopSession(issueId);
                break;
        }

        resp.sendRedirect(req.getContextPath() + "/browse/" + issueId);
    }

    private SessionAction getAction(String actionStr) {
        return SessionAction.getAction(actionStr)
                            .orElseThrow(() -> new RuntimeException("Unknown action " + actionStr));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String issueId = req.getParameter("issueId");
        sessionService.stopSession(issueId);

        resp.sendRedirect(req.getContextPath() + "/browse/" + issueId);
    }
}
