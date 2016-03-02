package com.scmspain.bigdata.emr.Configuration;

import com.amazonaws.services.elasticmapreduce.model.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class ApplicationsTest
{
    private Applications applications;

    @Before
    public void setUp() throws Exception
    {
        applications = new Applications("Hue,Oozie");
    }

    @After
    public void tearDown() throws Exception
    {
        applications = null;
    }

    @Test
    public void testGetApplicationsToInstall() throws Exception
    {
        Collection<Application> applicationsToInstall = applications.getApplicationsToInstall();

        assertEquals("There are two applications to install", applicationsToInstall.size(), 2);
    }
}