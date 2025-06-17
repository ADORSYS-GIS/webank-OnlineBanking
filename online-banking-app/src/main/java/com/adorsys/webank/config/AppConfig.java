package com.adorsys.webank.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Main application configuration class that enables and validates configuration properties.
 */
@Configuration
@EnableConfigurationProperties({ServerProperties.class, JwtProperties.class})
public class AppConfig {
    // Configuration properties are automatically registered and validated
}
