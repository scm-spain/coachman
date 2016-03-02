package com.scmspain.bigdata.emr.Steps;

import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;

import javax.inject.Inject;
import java.io.IOException;

public class StepsConfiguration
{
    private ReadConfigFile configFile;

    @Inject
    public StepsConfiguration(ReadConfigFile configFile) throws IOException
    {
        this.configFile = configFile;
    }

    public StepConfig getStepConfiguration()
    {
        StepConfig steps = null;

        if (null != configFile.getStepJar()) {
            steps = new StepConfig().withHadoopJarStep(new HadoopJarStepConfig()
                            .withJar(configFile.getStepJar())
                            .withArgs(configFile.getStepArguments())
            )
                    .withName(configFile.getStepName())
                    .withActionOnFailure(configFile.getActionOnFailure());
        }

        return steps;
    }
}
