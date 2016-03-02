package com.scmspain.bigdata.emr;

import com.amazonaws.services.elasticmapreduce.model.Configuration;
import com.amazonaws.services.elasticmapreduce.model.InstanceGroupConfig;
import com.amazonaws.services.elasticmapreduce.model.InstanceRoleType;
import com.scmspain.bigdata.emr.Configuration.ClusterConfiguration;
import com.scmspain.bigdata.emr.Configuration.ClusterProperties;
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
public class InstancesTest
{
    private Instances instances;

    @Mock
    private ClusterProperties properties;

    @Mock
    private ClusterConfiguration clusterConfiguration;


    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(InstancesTest.class);

        instances = new Instances(properties, clusterConfiguration);
    }

    @After
    public void tearDown() throws Exception
    {
        instances = null;
        properties = null;
        clusterConfiguration = null;
    }

    @Test
    public void testGetOnlyMasterInstance() throws Exception
    {
        getPropertiesForMasterInstance();
        returnNoCoreInstances();
        returnNoTaskInstances();

        ArrayList<InstanceGroupConfig> instancesList = (ArrayList<InstanceGroupConfig>) instances.getInstancesGroup();

        assertEquals("There are three elements on the list", 3, instancesList.size());
        assertEquals(
                "First element should be a master instance",
                InstanceRoleType.MASTER.toString(),
                instancesList.get(0).getInstanceRole()
        );
        assertNull("No core instances should be present", instancesList.get(1));
        assertNull("No task instances should be present", instancesList.get(2));
    }

    @Test
    public void testGetOnlyMasterAndCoreInstances() throws Exception
    {
        getPropertiesForMasterInstance();
        getPropertiesForCoreInstance();
        returnNoTaskInstances();

        ArrayList<InstanceGroupConfig> instancesList = (ArrayList<InstanceGroupConfig>) instances.getInstancesGroup();

        assertEquals("There are three elements on the list", 3, instancesList.size());
        assertEquals(
                "First element should be a master instance",
                InstanceRoleType.MASTER.toString(),
                instancesList.get(0).getInstanceRole()
        );
        assertEquals(
                "Second element should be a core instance",
                InstanceRoleType.CORE.toString(),
                instancesList.get(1).getInstanceRole()
        );

        assertNull("No task instances should be present", instancesList.get(2));
    }

    @Test
    public void testGetAllInstanceTypes() throws Exception
    {
        getPropertiesForMasterInstance();
        getPropertiesForCoreInstance();
        getPropertiesForTaskInstance();

        ArrayList<InstanceGroupConfig> instancesList = (ArrayList<InstanceGroupConfig>) instances.getInstancesGroup();

        assertEquals("There are three elements on the list", 3, instancesList.size());
        assertEquals(
                "First element should be a master instance",
                InstanceRoleType.MASTER.toString(),
                instancesList.get(0).getInstanceRole()
        );
        assertEquals(
                "Second element should be a core instance",
                InstanceRoleType.CORE.toString(),
                instancesList.get(1).getInstanceRole()
        );
        assertEquals(
                "Third element should be a task instance",
                InstanceRoleType.TASK.toString(),
                instancesList.get(2).getInstanceRole()
        );
    }

    @Test
    public void testWhenGettingPropertiesFail()
    {
        failWhenPropertiesDoesntExist();

        assertEquals(
                "When an error getting properties no instances should be present on list",
                0,
                instances.getInstancesGroup().size()
        );
    }

    private void getPropertiesForMasterInstance() throws Exception
    {
        when(properties.getPropMasterInstanceType()).thenReturn("m1.medium");
        when(properties.getPropName()).thenReturn("test");
        when(clusterConfiguration.getConfiguration()).thenReturn(new ArrayList<Configuration>(0));
    }

    private void getPropertiesForCoreInstance() throws Exception
    {
        when(properties.getPropSlaveCoreInstanceCount()).thenReturn(1);
        when(properties.getPropSlaveCoreInstanceType()).thenReturn("m1.medium");
        when(properties.getPropName()).thenReturn("test");
        when(clusterConfiguration.getConfiguration()).thenReturn(new ArrayList<Configuration>(0));
    }

    private void getPropertiesForTaskInstance() throws Exception
    {
        when(properties.getPropSlaveTaskInstanceCount()).thenReturn(1);
        when(properties.getPropSlaveTaskInstanceType()).thenReturn("m1.medium");
        when(properties.getPropName()).thenReturn("test");
        when(clusterConfiguration.getConfiguration()).thenReturn(new ArrayList<Configuration>(0));
    }

    private void returnNoTaskInstances()
    {
        when(properties.getPropSlaveTaskInstanceCount()).thenReturn(0);
    }

    private void returnNoCoreInstances()
    {
        when(properties.getPropSlaveCoreInstanceCount()).thenReturn(0);
    }

    private void failWhenPropertiesDoesntExist()
    {
        when(properties.getPropName()).thenThrow(Exception.class);
    }
}