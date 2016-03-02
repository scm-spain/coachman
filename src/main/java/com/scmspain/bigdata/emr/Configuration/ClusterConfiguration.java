package com.scmspain.bigdata.emr.Configuration;

import com.amazonaws.services.elasticmapreduce.model.Configuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scmspain.bigdata.emr.Filesystem.JsonConfiguration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;

public class ClusterConfiguration
{
    private static final String KEY_CLASSIFICATION = "classification";
    private static final String KEY_PROPERTIES = "properties";
    private static final String KEY_CONFIGURATIONS = "configurations";
    private static final String KEY_PASSWORD = "password";

    private String configurationFile;
    private JsonConfiguration jsonConfiguration;
    private PasswordDecrypt passwordDecrypt;

    @Inject
    public ClusterConfiguration(
            String configurationFile,
            JsonConfiguration jsonConfiguration,
            PasswordDecrypt passwordDecrypt
    )
    {
        this.configurationFile = configurationFile;
        this.jsonConfiguration = jsonConfiguration;
        this.passwordDecrypt = passwordDecrypt;
    }

    public ArrayList<Configuration> getConfiguration() throws Exception
    {
        ArrayList<Configuration> parsedConfiguration = new ArrayList<Configuration>();

        JsonArray json = jsonConfiguration.getJsonArrayFromFile(configurationFile);

        for (JsonElement configuration : json)
            parsedConfiguration.add(getConfigurationBranch(configuration.getAsJsonObject()));

        return parsedConfiguration;
    }

    private Configuration getConfigurationBranch(JsonObject jsonConfiguration)
    {
        if (!jsonConfiguration.has(KEY_CLASSIFICATION)) {
            return null;
        }

        Configuration configuration = new Configuration();

        configuration.withClassification(jsonConfiguration.get(KEY_CLASSIFICATION).getAsString());

        if (jsonConfiguration.has(KEY_PROPERTIES)) {
            for (Map.Entry<String, JsonElement> property : jsonConfiguration.get(KEY_PROPERTIES).getAsJsonObject().entrySet()) {
                String value = property.getValue().getAsString();

                if (property.getKey().equals(KEY_PASSWORD)) {
                    value = passwordDecrypt.decryptPassword(property.getValue().getAsString());
                }

                configuration.addPropertiesEntry(property.getKey(), value);
            }
        }

        if (jsonConfiguration.has(KEY_CONFIGURATIONS)) {
            JsonArray configurations = jsonConfiguration.get(KEY_CONFIGURATIONS).getAsJsonArray();
            for (JsonElement element : configurations) {
                configuration.withConfigurations(getConfigurationBranch(element.getAsJsonObject()));
            }
        }

        return configuration;
    }
}
