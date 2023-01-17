package com.aprey.jira.plugin.openpoker.config;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.security.xsrf.DoesNotRequireXsrfCheck;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webwork.action.Action;
import webwork.action.ServletActionContext;

import static com.aprey.jira.plugin.openpoker.api.config.ConfigurationManager.getStoredAllowedProjects;
import static com.aprey.jira.plugin.openpoker.api.config.ConfigurationManager.storeAllowedProjects;

@SupportedMethods({ RequestMethod.GET })
public class PokerConfigPage extends JiraWebActionSupport {

	//Logger instance for the class
	private static Logger log = LoggerFactory.getLogger(PokerConfigPage.class);
	//List of allowed projects
	private List<String> allowedProjects = new ArrayList<>();

	/**
	 * Method is called when the page is first loaded
	 */
	@Override
	public String doDefault() throws Exception {
		allowedProjects.addAll(getStoredAllowedProjects());
		return Action.INPUT;
	}

	/**
	 * Method is called when the form is submitted
	 */
	@Override
	@DoesNotRequireXsrfCheck
	@SupportedMethods({ RequestMethod.POST })
	protected String doExecute() throws Exception {
		String updatedAllowedProjects = getUpdatedAllowedProjects();
		storeAllowedProjects(updatedAllowedProjects);

		if ( !getHasErrorMessages() ) {
			return returnComplete("openPokerConfig!default.jspa");
		}
		return Action.INPUT;
	}

	/**
	 * Method to get the updated allowed projects from the form
	 *
	 * @return String of allowed projects
	 */
	private static String getUpdatedAllowedProjects() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String updatedAllowedProjects = request.getParameter("allowedProjects");
		return updatedAllowedProjects;
	}

	public List<String> getAllowedProjects() {
		return allowedProjects;
	}

	public String getAllowedProjectsValue() {
		return String.join(",", allowedProjects);
	}

	public void setAllowedProjects(List<String> allowedProjects) {
		this.allowedProjects = allowedProjects;
	}

	public List<Project> getProjects() {
		return ComponentAccessor.getProjectManager().getProjects();
	}

	public String getURL() {
		return "openPokerConfig.jspa";
	}
}
