package com.scmspain.bigdata.emr.s3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BucketParserTest
{
    BucketParser bucketParser;

    @Before
    public void setUp() throws Exception
    {
        bucketParser = new BucketParser();
    }

    @After
    public void tearDown() throws Exception
    {
        bucketParser = null;
    }

    @Test
    public void testGetBucketName() throws Exception
    {
        assertEquals(
                "Slashes and protocol should be removed from the bucket name",
                "big-deploy",
                bucketParser.withBucket("big-deploy/emr").getBucketName()
        );
    }

    @Test
    public void testGetBucketFolder() throws Exception
    {
        assertEquals(
                "First and final slash should be removed from the folder name",
                "emr/java",
                bucketParser.withBucket("big-deploy/emr/java/").getBucketFolder()
        );
    }
}