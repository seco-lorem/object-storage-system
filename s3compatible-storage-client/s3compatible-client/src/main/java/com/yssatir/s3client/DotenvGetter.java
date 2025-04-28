package com.yssatir.s3client;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvGetter {

    private static DotenvGetter single_instance = null;

    final public String DOTENV_DIRECTORY = "/Users/yekta/Desktop/sw_projects/work/application/companyname/storage-system";
    private Dotenv dotenv;
    
    private DotenvGetter() {

        this.dotenv = Dotenv.configure()
                .directory("/Users/yekta/Desktop/sw_projects/work/application/companyname/storage-system")
                .load();

    }
    
    public String getStorageEndpoint() {
    	return dotenv.get("STORAGE_ENDPOINT", "http://localhost:9000");
    }
    
    public String getStorageRegion() {
    	return dotenv.get("STORAGE_REGION", "us-east-1");
    }
    
    public String getAccessKey() {
    	return dotenv.get("ADMIN_USER");
    }
    
    public String getSecretKey() {
    	return dotenv.get("ADMIN_PASSWORD");
    }
    
    public String getOpaUrl(){
        return dotenv.get("OPA_URL", "http://localhost:8181/v1/data/minio/authz/allow");
    }

    // DotenvGetter is a Singleton class
    public static synchronized DotenvGetter getInstance() {
        if (single_instance == null)
            single_instance = new DotenvGetter();

        return single_instance;
    }

}
