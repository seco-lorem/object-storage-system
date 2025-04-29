package com.yssatir.s3client;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "upload-file", description = "Uploads a file to a given S3 bucket")
public class UploadFileCommand implements Runnable {

	private static DotenvGetter dotenvGetter = DotenvGetter.getInstance();
    private static final String ACCESS_KEY = dotenvGetter.getAccessKey();
	
    @CommandLine.Option(names = {"-b", "--bucket"}, required = true, description = "Bucket name")
    private String bucketName;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "File path to upload")
    private String filePath;

    @CommandLine.Option(names = {"-k", "--key"}, required = true, description = "S3 object key")
    private String objectKey;
    
    private long calculateProjectedSize(AmazonS3 s3, long fileSize) {

        ObjectListing listing = s3.listObjects(bucketName);
        long currentSize = 0;
        for (S3ObjectSummary summary : listing.getObjectSummaries()) {
            currentSize += summary.getSize();
        }

        return currentSize + fileSize;
        
    }
    
    private boolean checkPolicy(File file, AmazonS3 s3) {
    	// Calculate current bucket size
        long projectedSize;
        long fileSize = file.length();
        try {
	        projectedSize = calculateProjectedSize(s3, fileSize);
        } catch (Exception e) {
            System.err.println("Error calculating projected size: " + e.getMessage());
            return false;
        }

        // Check Policy
        return OpaPolicyClient.checkPolicy(ACCESS_KEY, "upload", bucketName, true, projectedSize);
        
    }
    
    private void uploadFile(AmazonS3 s3, File file) throws AmazonServiceException, AmazonClientException, InterruptedException {
    	// What ChatGPT generated was perfectly fine. I only had to read it properly, then writing myself was quite easy
        TransferManager transferManager = TransferManagerBuilder.standard()
            .withS3Client(s3)
            .build();
        PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, file);
        
        request.setGeneralProgressListener( new UploadProgressBarPrinter(file.length()));

        Upload upload = transferManager.upload(request);
        upload.waitForCompletion();
        System.out.println("\nFile uploaded as: " + objectKey);

        transferManager.shutdownNow();
    }

    @Override
    public void run() {
        AmazonS3 s3 = S3Service.getClient();
        File file = new File(filePath);
        
        if (!checkPolicy(file, s3)) {
            System.out.println("Upload denied by policy.");
        	return;
        }

        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        
        try {
        	uploadFile(s3, file);
        } catch (Exception e) {
            System.err.println("Upload failed: " + e.getMessage());
        }
    }
}
