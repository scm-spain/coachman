package com.scmspain.bigdata.emr.Configuration;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticmapreduce.model.Application;
import com.scmspain.bigdata.emr.Filesystem.PropertiesFile;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

public class ClusterProperties
{
    private static final String PROP_AWS_ZONE = "aws_zone";
    private static final String PROP_NAME = "name";
    private static final String PROP_RELEASE_LABEL = "release_label";
    private static final String PROP_SERVICE_ROLE = "service_role";
    private static final String PROP_JOBFLOW_ROLE = "jobflow_role";
    private static final String PROP_APPLICATIONS = "applications";
    private static final String PROP_KEY_NAME = "key_name";
    private static final String PROP_MASTER_INSTANCE_TYPE = "master_instance_type";
    private static final String PROP_SLAVE_CORE_INSTANCE_TYPE = "slave_core_instance_type";
    private static final String PROP_SLAVE_CORE_INSTANCE_COUNT = "slave_core_instance_count";
    private static final String PROP_SLAVE_TASK_INSTANCE_TYPE = "slave_task_instance_type";
    private static final String PROP_SLAVE_TASK_INSTANCE_COUNT = "slave_task_instance_count";
    private static final String PROP_SUBNET_ID = "subnet_id";
    private static final String PROP_VISIBLE_TO_ALL_USERS = "visible_to_all_users";
    private static final String PROP_LOGS_URI = "logs_uri";

    private static final String PROP_TAGS = "tags";
    private static Properties properties = null;

    private String filename;
    private PropertiesFile propertiesFile;

    @Inject
    public ClusterProperties(PropertiesFile propertiesFile, String filename)
    {
        this.propertiesFile = propertiesFile;
        this.filename = filename;
    }

    private Properties getProperties()
    {
        if (null == properties) {
            properties = propertiesFile.getClusterProperties(filename);
        }

        return properties;
    }

    public Region getPropAwsZone()
    {
        return Region.getRegion(Regions.fromName(getProperties().getProperty(PROP_AWS_ZONE)));
    }

    public String getPropName()
    {
        return getProperties().getProperty(PROP_NAME);
    }

    public String getPropReleaseLabel()
    {
        return getProperties().getProperty(PROP_RELEASE_LABEL);
    }

    public String getPropServiceRole()
    {
        return getProperties().getProperty(PROP_SERVICE_ROLE);
    }

    public String getPropJobFlowRole()
    {
        return getProperties().getProperty(PROP_JOBFLOW_ROLE);
    }

    public ArrayList<Application> getPropApplications()
    {
        return new Applications(getProperties().getProperty(PROP_APPLICATIONS)).getApplicationsToInstall();
    }

    public String getPropKeyName()
    {
        return getProperties().getProperty(PROP_KEY_NAME);
    }

    public String getPropMasterInstanceType()
    {
        return getProperties().getProperty(PROP_MASTER_INSTANCE_TYPE);
    }

    public String getPropSlaveCoreInstanceType()
    {
        return getProperties().getProperty(PROP_SLAVE_CORE_INSTANCE_TYPE);
    }

    public Integer getPropSlaveCoreInstanceCount()
    {
        return Integer.valueOf(getProperties().getProperty(PROP_SLAVE_CORE_INSTANCE_COUNT));
    }

    public String getPropSlaveTaskInstanceType()
    {
        return getProperties().getProperty(PROP_SLAVE_TASK_INSTANCE_TYPE);
    }

    public Integer getPropSlaveTaskInstanceCount()
    {
        return Integer.valueOf(getProperties().getProperty(PROP_SLAVE_TASK_INSTANCE_COUNT));
    }

    public String getPropSubnetId()
    {
        return getProperties().getProperty(PROP_SUBNET_ID);
    }

    public HashMap<String, String> getPropTags()
    {
        HashMap<String, String> tags = new HashMap<String, String>();

        for (String propertyKey : getProperties().stringPropertyNames()) {
            String[] split = propertyKey.split(Pattern.quote("."));

            if (split.length > 0 && split[0].equals(PROP_TAGS)) {
                tags.put(split[1], getProperties().getProperty(propertyKey));
            }
        }

        return tags;
    }

    public Boolean getPropVisibleToAllUsers()
    {
        return Boolean.valueOf(getProperties().getProperty(PROP_VISIBLE_TO_ALL_USERS));
    }

    public String getPropLogsUri()
    {
        return getProperties().getProperty(PROP_LOGS_URI);
    }
}
