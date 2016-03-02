package com.scmspain.bigdata.emr.Configuration;

import com.amazonaws.services.elasticmapreduce.model.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagsTest
{
    private Tags tags;

    @Mock
    ClusterProperties clusterProperties;

    @Before
    public void setUp() throws Exception
    {
        tags = new Tags(clusterProperties);
    }

    @After
    public void tearDown() throws Exception
    {
        tags = null;
    }

    @Test
    public void testGetTags() throws Exception
    {
        HashMap<String, String> mockedTags = new HashMap<String, String>();
        mockedTags.put("test", "ing");

        when(clusterProperties.getPropTags()).thenReturn(mockedTags);

        ArrayList<Tag> returnedTags = tags.getTags();

        assertEquals("Tags should be get from the cluster properties", 1, returnedTags.size());
    }
}