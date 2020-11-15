package it.com.aprey.jira.plugin.openpoker;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.aprey.jira.plugin.openpoker.api.MyPluginComponent;
import com.atlassian.sal.api.ApplicationProperties;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class MyComponentWiredTest
{
    private final ApplicationProperties applicationProperties;
    private final MyPluginComponent myPluginComponent;

    public MyComponentWiredTest(ApplicationProperties applicationProperties,MyPluginComponent myPluginComponent)
    {
        this.applicationProperties = applicationProperties;
        this.myPluginComponent = myPluginComponent;
    }

    @Test
    public void testMyName()
    {
        assertEquals("names do not match!", "myComponent:" + applicationProperties.getDisplayName(),myPluginComponent.getName());
    }
}