package ut.com.aprey.jira.plugin.openpoker;

import org.junit.Test;
import com.aprey.jira.plugin.openpoker.api.MyPluginComponent;
import com.aprey.jira.plugin.openpoker.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}