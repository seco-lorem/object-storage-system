package com.yssatir.s3client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import picocli.CommandLine;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

@CommandLine.Command(name = "download-file", description = "Downloads a file from a given S3 bucket")
public class DownloadFileCommand implements Runnable {
	
    @CommandLine.Option(names = {"-b", "--bucket"}, required = true, description = "Bucket name")
    private String bucketName;

    @CommandLine.Option(names = {"-k", "--key"}, required = true, description = "S3 object key")
    private String objectKey;

    @CommandLine.Option(names = {"-o", "--output"}, required = true, description = "Destination file path")
    private String outputFile;

    @Override
    public void run() {
        AmazonS3 s3Client = S3Service.getClient();

        try (S3Object object = s3Client.getObject(bucketName, objectKey);
             InputStream input = object.getObjectContent();
             OutputStream output = new FileOutputStream(Paths.get(outputFile).toFile())) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            System.out.println("File downloaded to: " + outputFile);
        } catch (Exception e) {
            System.err.println("Download failed: " + e.getMessage());
        }
    }
}
