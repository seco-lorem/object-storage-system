package com.yssatir.s3client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OpaPolicyClient {

	private static DotenvGetter dotenvGetter = DotenvGetter.getInstance();
    private static final String OPA_URL = dotenvGetter.getOpaUrl();

    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean checkPolicy(String user, String action, String bucketName, boolean versioning, long bucketSizeBytes) {
        try {
            // Create input map
            Map<String, Object> input = new HashMap<>();
            input.put("user", user);
            input.put("action", action);
            input.put("bucket", bucketName);
            input.put("bucket_versioning", versioning);
            input.put("bucket_size_bytes", bucketSizeBytes);

            Map<String, Object> payload = new HashMap<>();
            payload.put("input", input);

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

            // Read response
            Map<String, Object> response = mapper.readValue(conn.getInputStream(), new TypeReference<Map<String, Object>>() {});
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
