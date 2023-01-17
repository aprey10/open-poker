package com.aprey.jira.plugin.openpoker.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public abstract class ConfigurationManager {
	private static PluginSettingsFactory pluginSettingsFactory = ComponentAccessor.getOSGiComponentInstanceOfType(PluginSettingsFactory.class);

	//Key for plugin settings
	private static String OPEN_POKER_KEY = "com.aprey.jira.plugin.openpoker";

	/**
	 * Method to get the plugin settings
	 *
	 * @return pluginSettings instance
	 */
	public static PluginSettings getPluginSettings() {
		PluginSettingsFactory pluginSettingsFactory = ComponentAccessor.getOSGiComponentInstanceOfType(PluginSettingsFactory.class);
		PluginSettings pluginSettings = pluginSettingsFactory.createSettingsForKey(OPEN_POKER_KEY);
		return pluginSettings;
	}

	/**
	 * Method to get the stored allowed projects from plugin settings
	 *
	 * @return ArrayList of allowed projects
	 */
	public static ArrayList<String> getStoredAllowedProjects() {
		PluginSettings pluginSettings = getPluginSettings();
		String storedProjects = ((String) pluginSettings.get("allowedProjects"));
		if ( storedProjects != null && !storedProjects.isEmpty() ) {
			return Arrays.stream(storedProjects.split(",")).collect(Collectors.toCollection(ArrayList::new));
		}
		return new ArrayList<>();
	}

	/**
	 * Method to store the updated allowed projects in plugin settings
	 *
	 * @param updatedAllowedProjects String of allowed projects
	 */
	public static void storeAllowedProjects(String updatedAllowedProjects) {
		PluginSettings pluginSettings = getPluginSettings();
		pluginSettings.put("allowedProjects", updatedAllowedProjects);
	}
}
