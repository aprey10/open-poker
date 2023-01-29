package com.aprey.jira.plugin.openpoker.config;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import com.aprey.jira.plugin.openpoker.api.config.ConfigurationManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.mock.component.MockComponentWorker;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import webwork.action.ServletActionContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PokerConfigPageTest {

	private PokerConfigPage pokerConfigPage;
	private HttpServletRequest request;
	private Project project1;
	private Project project2;
	private Project project3;


	@InjectMocks
	private ProjectManager projectManager = mock(ProjectManager.class);

	@InjectMocks
	private PluginSettingsFactory pluginSettingsFactory = mock(PluginSettingsFactory.class);

	@InjectMocks
	private PluginSettings pluginSettings = mock(PluginSettings.class);

	@InjectMocks
	public static final ComponentAccessor componentAccessor = mock(ComponentAccessor.class);


	@Before
	public void setUp() {
		final MockComponentWorker mockComponentWorker = new MockComponentWorker();
		mockComponentWorker.addMock(ProjectManager.class, projectManager);
		mockComponentWorker.addMock(PluginSettings.class, pluginSettings);
		mockComponentWorker.addMock(PluginSettingsFactory.class, pluginSettingsFactory);
		mockComponentWorker.addMock(ComponentAccessor.class, componentAccessor);
		mockComponentWorker.init();

		when(pluginSettingsFactory.createSettingsForKey(anyString())).thenReturn(pluginSettings);

		pokerConfigPage = new PokerConfigPage();
		request = mock(HttpServletRequest.class);
		project1 = mock(Project.class);
		project2 = mock(Project.class);
		project3 = mock(Project.class);

		ServletActionContext.setRequest(request);
		when(request.getParameter("allowedProjects")).thenReturn("project1,project2");
		when(project1.getKey()).thenReturn("project1");
		when(project2.getKey()).thenReturn("project2");
		when(project3.getKey()).thenReturn("project3");
		when(projectManager.getProjects()).thenReturn(Arrays.asList(project1, project2, project3));
	}

	@Test
	public void testDoDefault() throws Exception {
		when(pluginSettings.get("allowedProjects")).thenReturn("project1,project2");
		assertEquals(JiraWebActionSupport.INPUT, pokerConfigPage.doDefault());
		assertEquals(Arrays.asList("project1", "project2"), pokerConfigPage.getAllowedProjects());
	}

	@Test
	public void testGetAllowedProjects() throws Exception {
		when(pluginSettings.get("allowedProjects")).thenReturn("project1,project2");
		ConfigurationManager.storeAllowedProjects("project1,project2");
		pokerConfigPage.doDefault();
		assertEquals(Arrays.asList("project1", "project2"), pokerConfigPage.getAllowedProjects());
	}

	@Test
	public void testGetAllowedProjectsValue() throws Exception {
		when(pluginSettings.get("allowedProjects")).thenReturn("project1,project2");
		ConfigurationManager.storeAllowedProjects("project1,project2");
		pokerConfigPage.doDefault();
		assertEquals("project1,project2", pokerConfigPage.getAllowedProjectsValue());
	}

	@Test
	public void testGetProjects() {
		List<Project> projects = pokerConfigPage.getProjects();
		assertEquals(3, projects.size());
		assertEquals("project1", projects.get(0).getKey());
		assertEquals("project2", projects.get(1).getKey());
		assertEquals("project3", projects.get(2).getKey());
	}

	@Test
	public void testGetURL() {
		assertEquals("openPokerConfig.jspa", pokerConfigPage.getURL());
	}
}
