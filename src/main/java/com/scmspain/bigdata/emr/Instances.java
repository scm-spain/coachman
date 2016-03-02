package com.scmspain.bigdata.emr;

import com.amazonaws.services.elasticmapreduce.model.InstanceGroupConfig;
import com.amazonaws.services.elasticmapreduce.model.InstanceRoleType;
import com.scmspain.bigdata.emr.Configuration.ClusterConfiguration;
import com.scmspain.bigdata.emr.Configuration.ClusterProperties;

import java.util.ArrayList;
import java.util.Collection;

class Instances
{
    private static final Integer MASTER_INSTANCE_COUNT_DEFAULT = 1;
    private static final String MASTER_INSTANCE_NAME_SUFFIX = "master";
    private static final String SLAVE_TASK_INSTANCE_NAME_SUFFIX = "slave_task";
    private static final String SLAVE_CORE_INSTANCE_NAME_SUFFIX = "slave_core";

    private ClusterProperties properties;
    private ClusterConfiguration clusterConfiguration;

    public Instances(ClusterProperties properties, ClusterConfiguration clusterConfiguration)
    {
        this.properties = properties;
        this.clusterConfiguration = clusterConfiguration;
    }

    public Collection<InstanceGroupConfig> getInstancesGroup()
    {
        try {
            ArrayList<InstanceGroupConfig> instancesList = new ArrayList<InstanceGroupConfig>(3);

            instancesList.add(getMasterInstanceGroup());
            instancesList.add(getCoreInstanceGroup());
            instancesList.add(getTaskInstanceGroup());

            return instancesList;
        } catch (Exception e) {
            return new ArrayList<InstanceGroupConfig>(0);
        }
    }

    private InstanceGroupConfig getMasterInstanceGroup() throws Exception
    {
        return new InstanceGroupConfig().withInstanceCount(MASTER_INSTANCE_COUNT_DEFAULT)
                .withInstanceRole(InstanceRoleType.MASTER)
                .withInstanceType(properties.getPropMasterInstanceType())
                .withConfigurations(clusterConfiguration.getConfiguration())
                .withName(properties.getPropName() + "_" + MASTER_INSTANCE_NAME_SUFFIX);
    }

    private InstanceGroupConfig getCoreInstanceGroup() throws Exception
    {
        return (0 < properties.getPropSlaveCoreInstanceCount()) ?
                new InstanceGroupConfig().withInstanceCount(properties.getPropSlaveCoreInstanceCount())
                        .withInstanceRole(InstanceRoleType.CORE)
                        .withInstanceType(properties.getPropSlaveCoreInstanceType())
                        .withConfigurations(clusterConfiguration.getConfiguration())
                        .withName(properties.getPropName() + "_" + SLAVE_CORE_INSTANCE_NAME_SUFFIX)
                : null;
    }

    private InstanceGroupConfig getTaskInstanceGroup() throws Exception
    {
        return (0 < properties.getPropSlaveTaskInstanceCount()) ?
                new InstanceGroupConfig().withInstanceCount(properties.getPropSlaveTaskInstanceCount())
                        .withInstanceRole(InstanceRoleType.TASK)
                        .withInstanceType(properties.getPropSlaveTaskInstanceType())
                        .withConfigurations(clusterConfiguration.getConfiguration())
                        .withName(properties.getPropName() + "_" + SLAVE_TASK_INSTANCE_NAME_SUFFIX)
                : null;
    }
}
