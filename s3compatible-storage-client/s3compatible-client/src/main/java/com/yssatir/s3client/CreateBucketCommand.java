package com.yssatir.s3client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "create-bucket",
        description = "Creates a new S3 bucket with required constraints",
        mixinStandardHelpOptions = true
)
public class CreateBucketCommand implements Callable<Integer> {

	private static DotenvGetter dotenvGetter = DotenvGetter.getInstance();
    private static final String ACCESS_KEY = dotenvGetter.getAccessKey();

    @CommandLine.Option(names = {"-n", "--name"}, description = "Bucket name", required = true)
    private String bucketName;
    
    private boolean checkPolicy() {
        try {
            boolean versioning = true;
            long initialSize = 0;
            if (!OpaPolicyClient.checkPolicy(ACCESS_KEY, "create_bucket", bucketName, versioning, initialSize)) {
                System.out.println("Policy denied bucket creation.");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error Checking Policy: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void createVersioningEnabledBucket(AmazonS3 s3){

        s3.createBucket(bucketName);
        System.out.println("Bucket created: " + bucketName);

        // Enable versioning
        BucketVersioningConfiguration configuration =
                new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED);
        s3.setBucketVersioningConfiguration(new SetBucketVersioningConfigurationRequest(bucketName, configuration));
        System.out.println("Versioning enabled on: " + bucketName);
        
    }

    @Override
    public Integer call() {
        if (!checkPolicy()) {
            return 1;
        }

        try {
            AmazonS3 s3 = S3Service.getClient();
            if (s3.doesBucketExistV2(bucketName)){
                System.out.println("Bucket already exists: " + bucketName);
                return 1;
            } 

            createVersioningEnabledBucket(s3);
            return 0;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
