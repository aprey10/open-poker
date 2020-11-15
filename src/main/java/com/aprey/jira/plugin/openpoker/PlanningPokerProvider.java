package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import java.util.HashMap;
import java.util.Map;

public class PlanningPokerProvider extends AbstractJiraContextProvider {

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("currentUsername", applicationUser.getUsername());


        return contextMap;
    }
}
