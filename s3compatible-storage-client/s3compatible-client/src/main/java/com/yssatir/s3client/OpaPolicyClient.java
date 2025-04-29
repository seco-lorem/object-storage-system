package com.yssatir.s3client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OpaPolicyClient {

	private static DotenvGetter dotenvGetter = DotenvGetter.getInstance();
    private static final String OPA_URL = dotenvGetter.getOpaUrl();

    private static final ObjectMapper mapper = new ObjectMapper();
    
    private static InputStream postRequestToOpa(Map<String, Object> payload) throws URISyntaxException, IOException {
        // Make request
        URI uri = new URI(OPA_URL);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = mapper.writeValueAsString(payload);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }
        
    	return conn.getInputStream();
    }
    
    private static Map<String, Object> createInputMap(String user, String action, String bucketName, boolean versioning, long bucketSizeBytes) {
        Map<String, Object> input = new HashMap<>();
        input.put("user", user);
        input.put("action", action);
        input.put("bucket", bucketName);
        input.put("bucket_versioning", versioning);
        input.put("bucket_size_bytes", bucketSizeBytes);
        return input;
    	
    }

    public static boolean checkPolicy(String user, String action, String bucketName, boolean versioning, long bucketSizeBytes) {
        try {
            // Create input map for debug print & to prepare opa request payload
        	Map<String, Object> input = createInputMap(user, action, bucketName, versioning, bucketSizeBytes);

            Map<String, Object> payload = new HashMap<>();
            payload.put("input", input);

            InputStream opaResponse = postRequestToOpa(payload);

            // Read response
            Map<String, Object> response = mapper.readValue(opaResponse, new TypeReference<Map<String, Object>>() {});
            Object result = response.get("result");

            if (Boolean.TRUE.equals(result)) {
                return true;
            } else {
                System.out.println("Policy denied the request.");
                System.out.println("Input:");
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input));
                
                return false;
            }

        } catch (Exception e) {
            System.err.println("Policy check failed: " + e.getMessage());
            return false;
        }
    }
}
