package com.scmspain.bigdata.emr.Filesystem;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonConfigurationTest
{
    private JsonConfiguration jsonConfiguration;

    @Mock
    private StringWriter stringWriter;

    @Mock
    private ConfigurationFile configurationFile;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(JsonConfigurationTest.class);

        jsonConfiguration = new JsonConfiguration(stringWriter, configurationFile, new JsonParser());
    }

    @After
    public void tearDown() throws Exception
    {
        jsonConfiguration = null;
        stringWriter = null;
        configurationFile = null;
    }

    @Test
    public void testGetJsonObjectFromFileFailsWhenReadingStream() throws Exception
    {
        errorReadingInputStream();

        jsonConfiguration.getJsonObjectFromFile("test");
    }

    @Test
    public void testGetJsonArrayFromFileFailsWhenReadingStream() throws Exception
    {
        errorReadingInputStream();

        jsonConfiguration.getJsonArrayFromFile("test");
    }

    @Test
    public void testGetJsonObjectFromFileWorksSuccessfully() throws FileNotFoundException
    {
        getFileAsInputStream();

        JsonObject json = jsonConfiguration.getJsonObjectFromFile("test");

        assertEquals(
                "JSON should have a key test",
                "\"unit\"",
                json.get("test").toString()
        );
    }

    @Test
    public void testGetJsonArrayFromFileWorksSuccessfully() throws FileNotFoundException
    {
        getFileAsInputStreamArray();

        JsonArray json = jsonConfiguration.getJsonArrayFromFile("testArray");

        int testArrayCount = json.size();
        assertEquals(
                "JSON array should contain two test elements",
                2,
                testArrayCount
        );
    }

    private void errorReadingInputStream() throws FileNotFoundException
    {
        when(configurationFile.getFileAsInputStream("test")).thenThrow(Exception.class);
    }

    private void getFileAsInputStream() throws FileNotFoundException
    {
        String jsonString = "{\"test\": \"unit\"}";
        InputStream stream = IOUtils.toInputStream(jsonString);
        when(configurationFile.getFileAsInputStream("test")).thenReturn(stream);

        when(stringWriter.toString()).thenReturn(jsonString);
    }

    private void getFileAsInputStreamArray() throws FileNotFoundException
    {
        String jsonString = "[{\"test\": \"unit\"}, {\"test2\": \"unit2\"}]";
        InputStream stream = IOUtils.toInputStream(jsonString);
        when(configurationFile.getFileAsInputStream("testArray")).thenReturn(stream);

        when(stringWriter.toString()).thenReturn(jsonString);
    }
}