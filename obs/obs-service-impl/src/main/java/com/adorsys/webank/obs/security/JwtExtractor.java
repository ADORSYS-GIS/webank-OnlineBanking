package com.adorsys.webank.obs.security;

import org.json.JSONException;
import org.json.JSONObject;

public class JwtExtractor {

    // Private constructor to prevent instantiation
    private JwtExtractor() {
        // Utility class; do not instantiate.
    }

    public static String extractPayloadHash(String payload) throws JSONException {
        // Parse the payload string into a JSONObject
        JSONObject jsonPayload = new JSONObject(payload);

        // Extract the "hash" field from the payload

        // Print the extracted hash
        return jsonPayload.getString("hash");
    }
}
