package com.aprey.jira.plugin.openpoker.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.mock.component.MockComponentWorker;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ConfigurationManagerTest {

	@InjectMocks
	private PluginSettingsFactory pluginSettingsFactory = mock(PluginSettingsFactory.class);

	@InjectMocks
	private PluginSettings pluginSettings = mock(PluginSettings.class);

	@InjectMocks
	public static final ComponentAccessor componentAccessor = mock(ComponentAccessor.class);

	@Before
	public void setUp() {
		final MockComponentWorker mockComponentWorker = new MockComponentWorker();
		mockComponentWorker.addMock(PluginSettings.class, pluginSettings);
		mockComponentWorker.addMock(PluginSettingsFactory.class, pluginSettingsFactory);
		mockComponentWorker.addMock(ComponentAccessor.class, componentAccessor);
		mockComponentWorker.init();

		when(pluginSettingsFactory.createSettingsForKey(anyString())).thenReturn(pluginSettings);
	}

	@Test
	public void testGetStoredAllowedProjects() {
		// Test with stored projects
		when(pluginSettings.get("allowedProjects")).thenReturn("project1,project2,project3");
		List<String> expected = Arrays.asList("project1", "project2", "project3");
		List<String> result = ConfigurationManager.getStoredAllowedProjects();
		assertEquals(expected, result);

		// Test with no stored projects
		when(pluginSettings.get("allowedProjects")).thenReturn(null);
		expected = new ArrayList<>();
		result = ConfigurationManager.getStoredAllowedProjects();
		assertEquals(expected, result);
	}

	@Test
	public void testStoreAllowedProjects() {
		// Test storing allowed projects
		String updatedAllowedProjects = "project1,project2,project3";
		ConfigurationManager.storeAllowedProjects(updatedAllowedProjects);
		verify(pluginSettings).put("allowedProjects", updatedAllowedProjects);
	}
}
