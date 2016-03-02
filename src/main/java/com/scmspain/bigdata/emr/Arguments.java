package com.scmspain.bigdata.emr;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.HashMap;

class Arguments
{
    private static final String ARG_CLUSTER_PROPERTIES = "properties";
    private static final String ARG_CLUSTER_CONFIGURATION = "configuration";
    private static final String ARG_STEPS_CONFIGURATION = "steps";
    private static final String ARG_FILES_TO_UPLOAD = "files";
    private static final String ARG_PASSWORD_SALT = "password-salt";

    private static final String DEFAULT_PROPERTIES_FILE = "default.properties";
    private static final String DEFAULT_CONFIGURATION_FILE = "default_configuration.json";

    private String[] args;
    private HashMap<String, String> arguments = new HashMap<String, String>();

    public Arguments(String[] args) throws Exception
    {
        this.args = args;
        parseArguments();
    }

    private void parseArguments() throws Exception
    {
        Options opt = new Options();

        opt.addOption("p", "properties-file", true, "Cluster properties to deploy");
        opt.addOption("c", "configuration", true, "Cluster configuration to deploy");
        opt.addOption("t", "steps", true, "Configuration of the steps to run when bootstrapping the cluster (comma separated to run multiple steps)");
        opt.addOption("f", "files", true, "Files to upload to s3, the format should be: s3://bucket/folder:/local/path/to/folder,s3://other_bucket/:/local/path/to/file");
        opt.addOption("s", "password-salt", true, "Salt to decrypt the passwords");

        try {
            CommandLine cl = new DefaultParser().parse(opt, this.args);

            arguments.put(ARG_CLUSTER_PROPERTIES, cl.hasOption("p") ? cl.getOptionValue("p") : DEFAULT_PROPERTIES_FILE);
            arguments.put(ARG_CLUSTER_CONFIGURATION, cl.hasOption("c") ? cl.getOptionValue("c") : DEFAULT_CONFIGURATION_FILE);
            arguments.put(ARG_STEPS_CONFIGURATION, cl.hasOption("t") ? cl.getOptionValue("t") : "");
            arguments.put(ARG_FILES_TO_UPLOAD, cl.hasOption("f") ? cl.getOptionValue("f") : "");
            arguments.put(ARG_PASSWORD_SALT, cl.hasOption("s") ? cl.getOptionValue("s") : "");
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getArgClusterProperties()
    {
        return arguments.get(ARG_CLUSTER_PROPERTIES);
    }

    public String getArgClusterConfiguration()
    {
        return arguments.get(ARG_CLUSTER_CONFIGURATION);
    }

    public String[] getArgStepsConfiguration()
    {
        return arguments.get(ARG_STEPS_CONFIGURATION).split(",");
    }

    public String[] getArgFilesToUpload()
    {
        return arguments.get(ARG_FILES_TO_UPLOAD).split(",");
    }

    public String getArgPasswordSalt()
    {
        return arguments.get(ARG_PASSWORD_SALT);
    }
}
