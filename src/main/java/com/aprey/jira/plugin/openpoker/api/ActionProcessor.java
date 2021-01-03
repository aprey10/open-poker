package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public interface ActionProcessor {

    default Optional<Integer> getParam(HttpServletRequest request, String paramName) {
        String param = request.getParameter(paramName);
        if (param == null || param.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(param));
        } catch (NumberFormatException ignore) {
            return Optional.empty();
        }
    }

    void process(PersistenceService persistenceService, HttpServletRequest request, String issueId);
}
