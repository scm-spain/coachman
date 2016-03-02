package com.scmspain.bigdata.emr;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.google.gson.JsonParser;
import com.scmspain.bigdata.emr.Configuration.ClusterConfiguration;
import com.scmspain.bigdata.emr.Configuration.ClusterProperties;
import com.scmspain.bigdata.emr.Configuration.PasswordDecrypt;
import com.scmspain.bigdata.emr.Configuration.Tags;
import com.scmspain.bigdata.emr.Filesystem.ConfigurationFile;
import com.scmspain.bigdata.emr.Filesystem.JsonConfiguration;
import com.scmspain.bigdata.emr.Filesystem.PropertiesFile;
import com.scmspain.bigdata.emr.Steps.ReadConfigFile;
import com.scmspain.bigdata.emr.Steps.StepsConfiguration;
import com.scmspain.bigdata.emr.s3.BucketParser;
import com.scmspain.bigdata.emr.s3.Uploader;

import java.io.StringWriter;
import java.util.ArrayList;

public class Coachman
{
    public static void main(String[] args)
    {
        try {
            Arguments arguments = new Arguments(args);

            PasswordDecrypt passwordDecrypt = new PasswordDecrypt(arguments.getArgPasswordSalt());

            Uploader uploader = new Uploader(
                    arguments.getArgFilesToUpload(),
                    new TransferManager(new DefaultAWSCredentialsProviderChain().getCredentials()),
                    new BucketParser()
            );
            uploader.uploadFiles();

            AmazonElasticMapReduceClient emr = new AmazonElasticMapReduceClient(
                    new DefaultAWSCredentialsProviderChain()
            );

            ClusterProperties prop = new ClusterProperties(
                    new PropertiesFile(new ConfigurationFile()),
                    arguments.getArgClusterProperties()
            );
            emr.setRegion(prop.getPropAwsZone());

            ArrayList<StepConfig> stepsConfigurations = new ArrayList<StepConfig>(arguments.getArgStepsConfiguration().length);
            for (String stepsConfigurationFile : arguments.getArgStepsConfiguration()) {
                stepsConfigurations.add(
                        new StepsConfiguration(
                                new ReadConfigFile(
                                        stepsConfigurationFile,
                                        new JsonConfiguration(
                                                new StringWriter(),
                                                new ConfigurationFile(),
                                                new JsonParser()
                                        ),
                                        passwordDecrypt
                                )
                        ).getStepConfiguration()
                );
            }

            RunJobFlowRequest request = new RunJobFlowRequest()
                    .withName(prop.getPropName())
                    .withReleaseLabel(prop.getPropReleaseLabel())
                    .withServiceRole(prop.getPropServiceRole())
                    .withJobFlowRole(prop.getPropJobFlowRole())
                    .withApplications(prop.getPropApplications())
                    .withTags(new Tags(prop).getTags())
                    .withVisibleToAllUsers(prop.getPropVisibleToAllUsers())
                    .withLogUri(prop.getPropLogsUri())
                    .withInstances(new JobFlowInstancesConfig()
                                    .withEc2KeyName(prop.getPropKeyName())
                                    .withKeepJobFlowAliveWhenNoSteps(true)
                                    .withEc2SubnetId(prop.getPropSubnetId())
                                    .withInstanceGroups(
                                            new Instances(
                                                    prop,
                                                    new ClusterConfiguration(
                                                            arguments.getArgClusterConfiguration(),
                                                            new JsonConfiguration(
                                                                    new StringWriter(),
                                                                    new ConfigurationFile(),
                                                                    new JsonParser()
                                                            ),
                                                            passwordDecrypt
                                                    )
                                            ).getInstancesGroup()
                                    )
                    );

            if (stepsConfigurations.size() > 0) {
                request.withSteps(stepsConfigurations);
            }

            RunJobFlowResult result = emr.runJobFlow(request);

            System.out.println("Cluster creation requested, id of the cluster: " + result.toString());


        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
