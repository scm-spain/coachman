package com.scmspain.bigdata.emr.s3;

public class BucketParser
{
    private String bucketURI;

    public BucketParser withBucket(String bucketURI)
    {
        this.bucketURI = bucketURI.replace("s3://", "");

        return this;
    }

    public String getBucketName()
    {
        return bucketURI.split("/")[0].replaceAll("/", "");
    }

    public String getBucketFolder()
    {
        String bucketFolder = bucketURI.replace(getBucketName(), "").replaceAll("(^/|/$)", "");

        return (bucketFolder.equals("/")) ? "" : bucketFolder;
    }
}
