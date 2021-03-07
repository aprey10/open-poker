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
    public PokerSessionServlet(PersistenceService persistenceService, ApplyVoteProcessor applyVoteProcessor) {
        this.persistenceService = checkNotNull(persistenceService);
        this.actionProcessorMap = ImmutableMap.<SessionAction, ActionProcessor>builder()
                .put(SessionAction.START_SESSION, new StartSessionProcessor())
                .put(SessionAction.STOP_SESSION, new StopSessionProcessor())
                .put(SessionAction.VOTE, new VoteProcessor())
                .put(SessionAction.RE_ESTIMATE, new StartSessionProcessor())
                .put(SessionAction.CANCEL, new DeleteSessionProcessor())
                .put(SessionAction.APPLY_ESTIMATE, applyVoteProcessor)
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
