package com.scmspain.bigdata.emr.Steps;

import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StepsConfigurationTest
{
    private StepsConfiguration stepsConfiguration;

    @Mock
    private ReadConfigFile configFile;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(StepsConfigurationTest.class);

        stepsConfiguration = new StepsConfiguration(configFile);
    }

    @After
    public void tearDown() throws Exception
    {
        stepsConfiguration = null;
        configFile = null;
    }

    @Test
    public void testGetStepConfigurationReturnsNullIfNoStepFound()
    {
        errorReadingInputStream();

        StepConfig config = stepsConfiguration.getStepConfiguration();
        assertNull("Returned step configuration should be null", config);
    }

    @Test
    public void testGetStepConfigurationReturnsStepConfigWhenConfigurationOk()
    {
        okReadingInputStream();

        StepConfig config = stepsConfiguration.getStepConfiguration();

        assertEquals("The step configuration should be of type StepConfig", StepConfig.class, config.getClass());
    }

    private void errorReadingInputStream()
    {
        when(configFile.getStepJar()).thenReturn(null);
    }

    private void okReadingInputStream()
    {
        ArrayList<String> arguments = new ArrayList<String>();
        arguments.add("hive-script");
        arguments.add("--version");

        when(configFile.getStepJar()).thenReturn("command-runner.jar");
        when(configFile.getStepArguments()).thenReturn(arguments);
        when(configFile.getStepName()).thenReturn("configuration");
        when(configFile.getActionOnFailure()).thenReturn(ActionOnFailure.CONTINUE);
    }

}