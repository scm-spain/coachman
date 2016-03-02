package com.scmspain.bigdata.emr.Configuration;

import com.amazonaws.services.elasticmapreduce.model.Configuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.scmspain.bigdata.emr.Filesystem.JsonConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClusterConfigurationTest
{
    private static final String CONFIG_FILE = "test_config.json";

    private ClusterConfiguration clusterConfiguration;

    @Mock
    private JsonConfiguration jsonConfiguration;

    @Mock
    private PasswordDecrypt passwordDecrypt;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(ClusterConfigurationTest.class);

        clusterConfiguration = new ClusterConfiguration(
                CONFIG_FILE,
                jsonConfiguration,
                passwordDecrypt
        );
    }

    @After
    public void tearDown() throws Exception
    {
        clusterConfiguration = null;
        jsonConfiguration = null;
        passwordDecrypt = null;
    }

    @Test
    public void testWhenConfigurationDoesNotHaveClassificationParentKey() throws Exception
    {
        jsonFileWithoutClassificationParentKey();

        ArrayList<Configuration> config = clusterConfiguration.getConfiguration();
        assertEquals("The configuration should contain only one value", 1, config.size());
        assertNull("The only element on the configuration should be null", config.get(0));
    }

    @Test
    public void testWhenConfigurationDoesNotHavePropertiesKey() throws Exception
    {
        jsonFileWithoutPropertiesKey();

        ArrayList<Configuration> config = clusterConfiguration.getConfiguration();
        assertEquals("The configuration should contain only one value", 1, config.size());
        assertEquals(
                "The only element on the configuration should be of type Configuration",
                Configuration.class,
                config.get(0).getClass()
        );
    }

    @Test
    public void testWhenConfigurationDoesNotHaveConfigurationsKey() throws Exception
    {
        jsonFileWithoutConfigurationsKey();

        ArrayList<Configuration> config = clusterConfiguration.getConfiguration();
        assertEquals("The configuration should contain only one value", 1, config.size());
        assertEquals(
                "The only element on the configuration should be of type Configuration",
                Configuration.class,
                config.get(0).getClass()
        );
    }

    @Test
    public void testWhenConfigurationHasAllTheKeys() throws Exception
    {
        jsonFileWithFullConfiguration();

        ArrayList<Configuration> config = clusterConfiguration.getConfiguration();
        assertEquals("The configuration should contain two values", 2, config.size());
        assertEquals(
                "The first element on the configuration should be of type Configuration",
                Configuration.class,
                config.get(0).getClass()
        );
        assertEquals(
                "The second element on the configuration should be of type Configuration",
                Configuration.class,
                config.get(1).getClass()
        );
    }

    private void jsonFileWithoutPropertiesKey() throws IOException
    {
        String jsonString = "["+
                "  {\"classification\": \"export\"}"+
                "]";
        JsonArray json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonArray();

        when(jsonConfiguration.getJsonArrayFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithoutClassificationParentKey() throws IOException
    {
        String jsonString = "["+
                "  {" +
                "    \"configurations\": [" +
                "      {" +
                "        \"classification\": \"export\"," +
                "        \"properties\": {" +
                "          \"OOZIE_URL\": \"http://localhost:11000/oozie\"" +
                "        }" +
                "      }" +
                "    ]" +
                "  }" +
                "]";
        JsonArray json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonArray();

        when(jsonConfiguration.getJsonArrayFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithoutConfigurationsKey() throws IOException
    {
        String jsonString = "["+
                "  {\"classification\": \"export\",\"properties\": " +
                "    {\"OOZIE_URL\": \"http://localhost:11000/oozie\"}" +
                "  }"+
                "]";
        JsonArray json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonArray();

        when(jsonConfiguration.getJsonArrayFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithFullConfiguration() throws IOException
    {
        String jsonString = "[" +
                "  {" +
                "    \"classification\": \"oozie-env\"," +
                "    \"configurations\": [" +
                "      {" +
                "        \"classification\": \"export\"," +
                "        \"properties\": {" +
                "          \"OOZIE_URL\": \"http://localhost:11000/oozie\"" +
                "        }" +
                "      }" +
                "    ]" +
                "  }," +
                "  {" +
                "    \"classification\": \"oozie-site\"," +
                "    \"properties\": {" +
                "      \"OOZIE_URL\": \"http://localhost:11000/oozie\"" +
                "    }" +
                "  }" +
                "]";
        JsonArray json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonArray();

        when(jsonConfiguration.getJsonArrayFromFile(CONFIG_FILE)).thenReturn(json);
    }
}