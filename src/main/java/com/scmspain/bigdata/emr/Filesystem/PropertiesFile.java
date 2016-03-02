package com.scmspain.bigdata.emr.Filesystem;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile
{
    private Properties properties = new Properties();
    private ConfigurationFile configurationFile;

    @Inject
    public PropertiesFile(ConfigurationFile configurationFile)
    {
        this.configurationFile = configurationFile;
    }

    public Properties getClusterProperties(String filename)
    {
        try {
            properties.load(configurationFile.getFileAsInputStreamReader(filename));
        } catch (IOException e) {

        }

        return properties;
    }
}
