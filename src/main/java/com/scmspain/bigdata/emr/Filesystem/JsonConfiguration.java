package com.scmspain.bigdata.emr.Filesystem;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.StringWriter;

public class JsonConfiguration
{
    private StringWriter writer;

    private ConfigurationFile configurationFile;

    private JsonParser jsonParser;

    @Inject
    public JsonConfiguration(StringWriter writer, ConfigurationFile configurationFile, JsonParser jsonParser)
    {
        this.writer = writer;
        this.configurationFile = configurationFile;
        this.jsonParser = jsonParser;
    }

    public JsonObject getJsonObjectFromFile(String filename)
    {
        try {
            IOUtils.copy(configurationFile.getFileAsInputStream(filename), writer, "UTF-8");

            return jsonParser
                    .parse(writer.toString())
                    .getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public JsonArray getJsonArrayFromFile(String filename)
    {
        try {
            IOUtils.copy(configurationFile.getFileAsInputStream(filename), writer, "UTF-8");

            return jsonParser
                    .parse(writer.toString())
                    .getAsJsonArray();
        } catch (Exception e) {
            return new JsonArray();
        }
    }
}
