package com.scmspain.bigdata.emr.s3;

import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;

import javax.inject.Inject;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Uploader
{
    private static final String KEY_LOCAL = "local";
    private static final String KEY_S3 = "s3";
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final Integer MINIMUM_NUMBER_OF_PARTS = 1;

    private String[] filesToUpload;
    private TransferManager s3Client;
    private BucketParser parser;

    @Inject
    public Uploader(String[] filesToUpload, TransferManager s3Client, BucketParser parser)
    {
        this.filesToUpload = filesToUpload;
        this.s3Client = s3Client;
        this.parser = parser;
    }

    public void uploadFiles()
    {
        Transfer upload;

        for (String aFilesToUpload : filesToUpload) {
            upload = getTransferInstance(aFilesToUpload);

            final Transfer finalUpload = upload;
            upload.addProgressListener(new ProgressListener() {
                public void progressChanged(ProgressEvent progressEvent) {
                    if (progressEvent.getEventType().isTransferEvent()) {
                        System.out.println(
                                finalUpload.getDescription()
                                        + " (" + df.format(finalUpload.getProgress().getPercentTransferred()) + "%)"
                        );
                    }
                }
            });

            try {
                upload.waitForCompletion();
                System.out.println("Upload complete!!!");
            } catch (InterruptedException e) {
                System.out.println("Error uploading file to S3");
            }
        }

        s3Client.shutdownNow();

    }

    private HashMap<String, String> parseFileToUpload(String file)
    {
        String[] fileExploded = file.replace("s3://", "").split(":");

        HashMap<String, String> files = new HashMap<String, String>();

        if (MINIMUM_NUMBER_OF_PARTS < fileExploded.length) {
            files.put(KEY_LOCAL, fileExploded[0]);
            files.put(KEY_S3, fileExploded[1]);
        }

        return files;
    }

    private String getS3Filename(String bucketFolder, String filename)
    {
        return (bucketFolder + "/" + filename).replaceAll("/{2,}", "/");
    }

    private Transfer getTransferInstance(String filesToUpload)
    {
        HashMap<String, String> toUpload = parseFileToUpload(filesToUpload);
        File localFile = new File(toUpload.get(KEY_LOCAL));

        parser.withBucket(toUpload.get(KEY_S3));
        return  (localFile.isDirectory()) ?
                s3Client.uploadDirectory(
                        parser.getBucketName(),
                        getS3Filename(parser.getBucketFolder(), localFile.getName()),
                        localFile,
                        true
                ) :
                s3Client.upload(
                        parser.getBucketName(),
                        getS3Filename(parser.getBucketFolder(), localFile.getName()),
                        localFile
                );
    }
}
