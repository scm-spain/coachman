package com.scmspain.bigdata.emr.Configuration;


import com.amazonaws.services.elasticmapreduce.model.Application;

import java.util.ArrayList;

class Applications
{
    private String applications;

    public Applications(String applications)
    {
        this.applications = applications;
    }

    public ArrayList<Application> getApplicationsToInstall()
    {
        ArrayList<Application> applicationsToInstall = new ArrayList<Application>();

        String[] applicationsSplitted = applications.split(",");

        for (String anApplicationsSplitted : applicationsSplitted) {
            applicationsToInstall.add(new Application().withName(anApplicationsSplitted));
        }

        return applicationsToInstall;
    }
}
