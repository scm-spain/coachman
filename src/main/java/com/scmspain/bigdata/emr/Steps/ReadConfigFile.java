package com.scmspain.bigdata.emr.Steps;

import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scmspain.bigdata.emr.Configuration.PasswordDecrypt;
import com.scmspain.bigdata.emr.Filesystem.JsonConfiguration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadConfigFile
{
    private static final String STEP_NAME = "name";
    private static final String STEP_JAR = "jar";
    private static final String STEP_ARGUMENTS = "arguments";
    private static final String STEP_ACTION_ON_FAILURE = "action_on_failure";
    private static final String DEFAULT_ACTION_ON_FAILURE = "terminate";
    private static final String DECRYPT_PASSWORD_KEY = "decryptPassword=";
    private static final String DECRYPT_PASSWORD_SEPARATOR = "=";

    private static final HashMap<String, ActionOnFailure> actionsOnFailure = new HashMap<String, ActionOnFailure>();

    static {
        actionsOnFailure.put("cancel", ActionOnFailure.CANCEL_AND_WAIT);
        actionsOnFailure.put("continue", ActionOnFailure.CONTINUE);
        actionsOnFailure.put("terminate_job", ActionOnFailure.TERMINATE_JOB_FLOW);
        actionsOnFailure.put("terminate", ActionOnFailure.TERMINATE_CLUSTER);
    }

    private String configurationFile;
    private JsonConfiguration jsonConfiguration;
    private PasswordDecrypt passwordDecrypt;
    private JsonObject configuration;

    @Inject
    public ReadConfigFile(String configurationFile, JsonConfiguration jsonConfiguration, PasswordDecrypt passwordDecrypt)
    {
        this.configurationFile = configurationFile;
        this.jsonConfiguration= jsonConfiguration;
        this.passwordDecrypt = passwordDecrypt;
        this.configuration = jsonConfiguration.getJsonObjectFromFile(configurationFile);
    }

    public String getStepName()
    {
        return (configuration.has(STEP_NAME)) ? configuration.get(STEP_NAME).getAsString() : null;
    }

    public String getStepJar()
    {
        return (configuration.has(STEP_JAR)) ? configuration.get(STEP_JAR).getAsString() : null;
    }

    public ArrayList<String> getStepArguments()
    {
        JsonArray arguments = configuration.getAsJsonArray(STEP_ARGUMENTS);

        if (arguments == null) return new ArrayList<String>();

        ArrayList<String> jarArguments = new ArrayList<String>(arguments.size());

        for (JsonElement argument : arguments) {
            String parsedArgument = argument.getAsString();
            if (parsedArgument.contains(DECRYPT_PASSWORD_KEY))
            {
                String[] passwordArgument = parsedArgument.split(DECRYPT_PASSWORD_SEPARATOR);
                parsedArgument = passwordDecrypt.decryptPassword(passwordArgument[1]);
            }

            jarArguments.add(parsedArgument);
        }

        return jarArguments;
    }

    public ActionOnFailure getActionOnFailure()
    {
        JsonObject configuration = jsonConfiguration.getJsonObjectFromFile(configurationFile);

        String actionOnFailure = (configuration.has(STEP_ACTION_ON_FAILURE)) ? configuration.get(STEP_ACTION_ON_FAILURE).getAsString() : null;

        return (actionsOnFailure.containsKey(actionOnFailure)) ?
                actionsOnFailure.get(actionOnFailure) :
                actionsOnFailure.get(DEFAULT_ACTION_ON_FAILURE);
    }
}
