package com.scmspain.bigdata.emr.Steps;

import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scmspain.bigdata.emr.Configuration.PasswordDecrypt;
import com.scmspain.bigdata.emr.Filesystem.JsonConfiguration;
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
public class ReadConfigFileTest
{
    private static final String CONFIG_FILE = "test_config.json";
    private static final String ENCRYPTED_PASS = "3nCrypted";
    private static final String DECRYPTED_PASS = "password";
    private static final String CONTINUE_ACTION_ON_FAILURE_NAME = "continue";
    private static final ActionOnFailure DEFAULT_ACTION_ON_FAILURE = ActionOnFailure.TERMINATE_CLUSTER;
    private static final ActionOnFailure CONTINUE_ACTION_ON_FAILURE = ActionOnFailure.CONTINUE;


    private ReadConfigFile readConfigFile;

    @Mock
    private JsonConfiguration jsonConfiguration;

    @Mock
    private PasswordDecrypt passwordDecrypt;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(ReadConfigFileTest.class);
    }

    @After
    public void tearDown() throws Exception
    {
        readConfigFile = null;
        jsonConfiguration = null;
        passwordDecrypt = null;
    }

    @Test
    public void testGetStepNameReturnsNullIfKeyNotFound()
    {
        jsonFileWithoutName();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        String name = readConfigFile.getStepName();
        assertNull("Returned name should be null if name key is not present in config", name);
    }

    @Test
    public void testGetStepNameReturnsNameIfKeyFound()
    {
        String expectedName = "Run test.sh";

        jsonFileWithFullConfiguration();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        String name = readConfigFile.getStepName();
        assertEquals("Returned name should be the expected one if name key found", expectedName, name);
    }

    @Test
    public void testGetStepJarReturnsNullIfKeyNotFound()
    {
        jsonFileWithoutJar();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        String jar = readConfigFile.getStepJar();
        assertNull("Returned jar should be null if jar key is not present in config", jar);
    }

    @Test
    public void testGetStepJarReturnsNameIfKeyFound()
    {
        String expectedJar = "path/to/jar.jar";

        jsonFileWithFullConfiguration();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        String jar = readConfigFile.getStepJar();
        assertEquals("Returned jar should be the expected one if jar key found", expectedJar, jar);
    }

    @Test
    public void testGetStepArgumentsReturnsEmptyArrayIfKeyNotFound()
    {
        jsonFileWithoutArguments();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        ArrayList<String> arguments = readConfigFile.getStepArguments();
        assertEquals("Arguments list should be empty if arguments key is not present in config", 0, arguments.size());
    }

    @Test
    public void testGetStepArgumentsReturnsArgumentsListIfKeyFound()
    {
        jsonFileWithFullConfiguration();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        ArrayList<String> arguments = readConfigFile.getStepArguments();
        assertEquals("The arguments list should contain two values", 2, arguments.size());
        assertEquals(
                "The first element on the arguments list should be of type String",
                String.class,
                arguments.get(0).getClass()
        );
        assertEquals(
                "The second element on the arguments list should be of type String",
                String.class,
                arguments.get(1).getClass()
        );
    }

    @Test
    public void testGetStepArgumentsDecryptsArgumentsIfKeyFound()
    {
        jsonFileWithFullConfigurationAndEncryptedPassword();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        ArrayList<String> arguments = readConfigFile.getStepArguments();
        assertEquals("The arguments list should contain two values", 2, arguments.size());
        assertEquals(
                "The first element on the arguments list should be of type String",
                String.class,
                arguments.get(0).getClass()
        );
        assertEquals(
                "The second element on the arguments list should be of type String",
                String.class,
                arguments.get(1).getClass()
        );
        assertEquals(
                "The encrypted element should have been decrypted",
                DECRYPTED_PASS,
                arguments.get(1)
        );
    }

    @Test
    public void testGetActionOnFailureReturnsDefaultActionIfKeyNotFound()
    {
        jsonFileWithoutActionOnFailure();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        ActionOnFailure action = readConfigFile.getActionOnFailure();
        assertEquals("Returned action on failure should be the default one", DEFAULT_ACTION_ON_FAILURE, action);
    }

    @Test
    public void testGetActionOnFailureReturnsActionIfKeyFound()
    {
        jsonFileWithFullConfiguration();
        readConfigFile = new ReadConfigFile(CONFIG_FILE, jsonConfiguration, passwordDecrypt);

        ActionOnFailure action = readConfigFile.getActionOnFailure();
        assertEquals("Returned action on failure should be" + CONTINUE_ACTION_ON_FAILURE_NAME, CONTINUE_ACTION_ON_FAILURE, action);
    }

    private void jsonFileWithoutName()
    {
        String jsonString =
                "{" +
                "    \"jar\": \"path/to/jar.jar\"," +
                "    \"arguments\": [" +
                "        \"first_argument\"," +
                "        \"second_argument\"" +
                "     ]," +
                "    \"action_on_failure\": \"terminate\"" +
                "}";
        JsonObject json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonObject();

        when(jsonConfiguration.getJsonObjectFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithoutJar()
    {
        String jsonString =
                "{" +
                "    \"name\": \"Run test.sh\"," +
                "    \"arguments\": [" +
                "        \"first_argument\"," +
                "        \"second_argument\"" +
                "     ]," +
                "    \"action_on_failure\": \"terminate\"" +
                "}";
        JsonObject json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonObject();

        when(jsonConfiguration.getJsonObjectFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithoutArguments()
    {
        String jsonString =
                "{" +
                "    \"name\": \"Run test.sh\"," +
                "    \"jar\": \"path/to/jar.jar\"," +
                "    \"action_on_failure\": \"terminate\"" +
                "}";
        JsonObject json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonObject();

        when(jsonConfiguration.getJsonObjectFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithoutActionOnFailure()
    {
        String jsonString =
                "{" +
                "    \"name\": \"Run test.sh\"," +
                "    \"jar\": \"path/to/jar.jar\"," +
                "    \"arguments\": [" +
                "        \"first_argument\"," +
                "        \"second_argument\"" +
                "     ]" +
                "}";
        JsonObject json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonObject();

        when(jsonConfiguration.getJsonObjectFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithFullConfiguration()
    {
        String jsonString =
                "{" +
                "    \"name\": \"Run test.sh\"," +
                "    \"jar\": \"path/to/jar.jar\"," +
                "    \"arguments\": [" +
                "        \"first_argument\"," +
                "        \"second_argument\"" +
                "     ]," +
                "    \"action_on_failure\": \"" + CONTINUE_ACTION_ON_FAILURE_NAME +"\"" +
                "}";
        JsonObject json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonObject();

        when(jsonConfiguration.getJsonObjectFromFile(CONFIG_FILE)).thenReturn(json);
    }

    private void jsonFileWithFullConfigurationAndEncryptedPassword()
    {
        String jsonString =
                "{" +
                "    \"name\": \"Run test.sh\"," +
                "    \"jar\": \"path/to/jar.jar\"," +
                "    \"arguments\": [" +
                "        \"first_argument\"," +
                "        \"decryptPassword=" + ENCRYPTED_PASS + "\"" +
                "     ]," +
                "    \"action_on_failure\": \"terminate\"" +
                "}";
        JsonObject json = (new JsonParser())
                .parse(jsonString)
                .getAsJsonObject();

        when(passwordDecrypt.decryptPassword(ENCRYPTED_PASS)).thenReturn(DECRYPTED_PASS);
        when(jsonConfiguration.getJsonObjectFromFile(CONFIG_FILE)).thenReturn(json);
    }

}