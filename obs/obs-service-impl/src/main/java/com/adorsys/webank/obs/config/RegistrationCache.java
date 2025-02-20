package com.adorsys.webank.obs.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RegistrationCache {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationCache.class);
    private final Cache<String, Boolean> cache;

    public RegistrationCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000) // Maximum size of the cache
                .build();
    }

    public void addToCache(String phoneNumber) {
        logger.info("Adding phone number to registration cache: {}", phoneNumber);
        cache.put(phoneNumber, true);
    }

    public boolean isRegistered(String phoneNumber) {
        boolean isRegistered = cache.getIfPresent(phoneNumber) != null;
        logger.info("Checking registration cache for phone number: {}. Registered: {}", phoneNumber, isRegistered);
        return isRegistered;
    }
}
