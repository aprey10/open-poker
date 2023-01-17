package com.aprey.jira.plugin.openpoker.api.config;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.mock.component.MockComponentWorker;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllowedProjectsConditionTest {

	@InjectMocks
	private PluginSettingsFactory pluginSettingsFactory = mock(PluginSettingsFactory.class);

	@InjectMocks
	private PluginSettings pluginSettings = mock(PluginSettings.class);
	@InjectMocks
	public static final ComponentAccessor componentAccessor = mock(ComponentAccessor.class);
	private AllowedProjectsCondition condition;
	private ApplicationUser applicationUser;
	private JiraHelper jiraHelper;
	private Project project;



	@Before
	public void setUp() {
		final MockComponentWorker mockComponentWorker = new MockComponentWorker();
		mockComponentWorker.addMock(PluginSettings.class, pluginSettings);
		mockComponentWorker.addMock(PluginSettingsFactory.class, pluginSettingsFactory);
		mockComponentWorker.addMock(ComponentAccessor.class, componentAccessor);
		mockComponentWorker.init();

		condition = new AllowedProjectsCondition();
		applicationUser = mock(ApplicationUser.class);
		jiraHelper = mock(JiraHelper.class);
		project = mock(Project.class);

		when(pluginSettingsFactory.createSettingsForKey(anyString())).thenReturn(pluginSettings);
	}

	@Test
	public void testShouldDisplay_AllowedProjectsIsEmpty() {
		ConfigurationManager.storeAllowedProjects("");
		when(pluginSettings.get("allowedProjects")).thenReturn("");
		assertTrue(condition.shouldDisplay(applicationUser, jiraHelper));
	}

	@Test
	public void testShouldDisplay_CurrentProjectIsAllowed() {
		when(jiraHelper.getProject()).thenReturn(project);
		when(project.getKey()).thenReturn("TEST");

		ConfigurationManager.storeAllowedProjects("TEST,ANOTHER");
		when(pluginSettings.get("allowedProjects")).thenReturn("TEST,ANOTHER");
		assertTrue(condition.shouldDisplay(applicationUser, jiraHelper));
	}

	@Test
	public void testShouldDisplay_CurrentProjectIsNotAllowed() {
		when(jiraHelper.getProject()).thenReturn(project);
		when(project.getKey()).thenReturn("NOT_ALLOWED");

		ConfigurationManager.storeAllowedProjects("TEST,ANOTHER");
		when(pluginSettings.get("allowedProjects")).thenReturn("TEST,ANOTHER");
		assertFalse(condition.shouldDisplay(applicationUser, jiraHelper));
	}

	@Test
	public void testShouldDisplay_NoCurrentProject() {
		when(jiraHelper.getProject()).thenReturn(null);
		assertTrue(condition.shouldDisplay(applicationUser, jiraHelper));
	}
}
