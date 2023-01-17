package com.aprey.jira.plugin.openpoker.api.config;

import java.util.List;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;

/**
 * Class that defines a condition for the web fragment visibility
 */
public class AllowedProjectsCondition extends AbstractWebCondition {

	/**
	 * Method to determine whether the web fragment should be displayed or not
	 * @param applicationUser The user currently viewing the page
	 * @param jiraHelper The JiraHelper that provides access to various Jira-related objects
	 * @return boolean value indicating whether the web fragment should be displayed or not
	 */
	@Override
	public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
		List<String> allowedProjects = ConfigurationManager.getStoredAllowedProjects();
		if ( allowedProjects.isEmpty() ) {
			return true;
		}
		if ( jiraHelper.getProject() != null ) {
			return allowedProjects.contains(jiraHelper.getProject().getKey());
		}
		return true;
	}
}
