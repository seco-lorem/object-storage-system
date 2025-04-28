package com.yssatir.s3client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


public class S3Service {

	private static DotenvGetter dotenvGetter = DotenvGetter.getInstance();
	
    private static final String ENDPOINT = dotenvGetter.getStorageEndpoint();
    private static final String REGION = dotenvGetter.getStorageRegion();
    private static final String ACCESS_KEY = dotenvGetter.getAccessKey();
    private static final String SECRET_KEY = dotenvGetter.getSecretKey();

    public static AmazonS3 getClient() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
