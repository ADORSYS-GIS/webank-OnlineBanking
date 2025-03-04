package com.adorsys.webank.obs.security;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtExtractorTest {

    @Test
    void extractPayloadHash_validHash_returnsHash() throws JSONException {
        String payload = "{\"hash\":\"testHash\"}";
        String hash = JwtExtractor.extractPayloadHash(payload);
        assertEquals("testHash", hash);
    }

    @Test
    void extractPayloadHash_missingHash_throwsException() {
        String payload = "{\"noHash\":\"value\"}";
        assertThrows(JSONException.class, () -> JwtExtractor.extractPayloadHash(payload));
    }
}