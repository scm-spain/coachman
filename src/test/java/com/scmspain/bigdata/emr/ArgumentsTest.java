package com.scmspain.bigdata.emr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArgumentsTest
{
    private static final String ARG_VALUE = "abcdefg";

    private Arguments arguments;

    @Before
    public void setUp() throws Exception
    {
        String[] args = new String[8];
        args[0] = "--properties-file";
        args[1] = ARG_VALUE;
        args[2] = "--configuration";
        args[3] = ARG_VALUE;
        args[4] = "--steps";
        args[5] = ARG_VALUE;
        args[6] = "--files";
        args[7] = ARG_VALUE;

        arguments = new Arguments(args);
    }

    @After
    public void tearDown() throws Exception
    {
        arguments = null;
    }

    @Test
    public void testGetArgClusterProperties() throws Exception
    {
        assertEquals("Cluster properties should be the one provided", arguments.getArgClusterProperties(), ARG_VALUE);
    }

    @Test
    public void testGetArgClusterConfiguration() throws Exception
    {
        assertEquals("Cluster configuration should be the one provided", arguments.getArgClusterConfiguration(), ARG_VALUE);
    }

    @Test
    public void testGetArgStepsConfiguration()
    {
        String[] expected = new String[1];
        expected[0] = ARG_VALUE;

        assertEquals("Steps to configure the cluster should be provided", arguments.getArgStepsConfiguration(), expected);
    }

    @Test
    public void testGetArgFilesToUpload()
    {
        String[] expected = new String[1];
        expected[0] = ARG_VALUE;

        assertEquals("Files to upload to the cluster should be provided", arguments.getArgFilesToUpload(), expected);
    }
}