package com.scmspain.bigdata.emr.Configuration;

import com.amazonaws.services.elasticmapreduce.model.Tag;

import java.util.ArrayList;
import java.util.HashMap;

public class Tags
{
    private ClusterProperties properties;

    public Tags(ClusterProperties properties)
    {
        this.properties = properties;
    }

    public ArrayList<Tag> getTags()
    {
        HashMap<String, String> clusterTags = properties.getPropTags();
        ArrayList<Tag> tags = new ArrayList<Tag>(clusterTags.size());

        for (String key : clusterTags.keySet()) {
            tags.add(new Tag().withKey(key).withValue(clusterTags.get(key)));
        }

        return tags;
    }
}
