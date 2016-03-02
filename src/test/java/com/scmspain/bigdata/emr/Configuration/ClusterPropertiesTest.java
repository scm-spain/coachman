package com.scmspain.bigdata.emr.Configuration;

import com.scmspain.bigdata.emr.Filesystem.PropertiesFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClusterPropertiesTest
{
    private ClusterProperties properties;

    @Mock
    private PropertiesFile propertiesFile;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(ClusterPropertiesTest.class);

        properties = new ClusterProperties(propertiesFile, "test");
    }

    @After
    public void tearDown() throws Exception
    {
        properties = null;
        propertiesFile = null;
    }

    @Test(expected=IOException.class)
    public void testWhenGettingPropertiesFileFails() throws Exception
    {
        gettingPropertiesFileAsStreamFails();

        assertNull("If the properties file can't be read all the properties are null", properties.getPropName());
    }

    @Test
    public void testGettingPropertiesFileWorks() throws IOException
    {
        gettingPropertiesFileSuccessfully();

        assertEquals(
                "Properties file is read and name is returned from it",
                "test",
                properties.getPropName()
        );
    }

    private void gettingPropertiesFileAsStreamFails() throws IOException
    {
        when(propertiesFile.getClusterProperties("test")).thenThrow(IOException.class);
    }

    private void gettingPropertiesFileSuccessfully() throws IOException
    {
        Properties clusterProperties = new Properties();
        clusterProperties.setProperty("name", "test");

        when(propertiesFile.getClusterProperties("test")).thenReturn(clusterProperties);
    }
}