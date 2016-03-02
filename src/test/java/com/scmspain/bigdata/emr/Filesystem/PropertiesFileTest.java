package com.scmspain.bigdata.emr.Filesystem;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesFileTest
{
    private PropertiesFile propertiesFile;

    @Mock
    private ConfigurationFile configurationFile;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(PropertiesFileTest.class);

        propertiesFile = new PropertiesFile(configurationFile);
    }

    @After
    public void tearDown() throws Exception
    {
        propertiesFile = null;
        configurationFile = null;
    }

    @Test
    public void testGetClusterPropertiesWhenTheFileCanBeRead() throws Exception
    {
        getTheInputStreamReaderFromTheFile();

        Properties prop = propertiesFile.getClusterProperties("test");

        assertEquals(
                "One property has been found on the file",
                1,
                prop.size()
        );
    }

    @Test
    public void testGettingTheClusterPropertiesFail() throws IOException
    {
        whenGettingTheInputStreamFails();
        Properties properties = propertiesFile.getClusterProperties("test");

        assertEquals(
                "Properties are empty because it failed to read",
                0,
                properties.size()
        );
    }

    private void getTheInputStreamReaderFromTheFile() throws FileNotFoundException
    {
        InputStream stream = IOUtils.toInputStream("test=unit");
        when(configurationFile.getFileAsInputStreamReader("test")).thenReturn(new InputStreamReader(stream));
    }

    private void whenGettingTheInputStreamFails() throws FileNotFoundException
    {
        when(configurationFile.getFileAsInputStreamReader("test")).thenThrow(IOException.class);
    }
}