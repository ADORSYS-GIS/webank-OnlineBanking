package com.adorsys.webank.obs.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PhoneNumberCache {
    private static final Logger logger = LoggerFactory.getLogger(PhoneNumberCache.class);
    private final Cache<String, Boolean> cache;


    public PhoneNumberCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build();
    }

    public void addToCache(String phoneNumber) {
        logger.info("Adding phone number to cache: {}", phoneNumber);
        cache.put(phoneNumber, true);
    }

    public boolean isInCache(String phoneNumber) {
        boolean isInCache = cache.getIfPresent(phoneNumber) != null;
        logger.info("Checking cache for phone number: {}. Exists: {}", phoneNumber, isInCache);
        return isInCache;
    }

    public void removeFromCache(String phoneNumber) {
        logger.info("Removing phone number from cache: {}", phoneNumber);
        cache.invalidate(phoneNumber);
    }
}
