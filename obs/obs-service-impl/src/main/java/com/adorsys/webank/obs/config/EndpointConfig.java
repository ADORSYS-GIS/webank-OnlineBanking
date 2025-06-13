package com.adorsys.webank.obs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.adorsys.webank.security.extractor.EndpointParameterMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class EndpointConfig {

    @Bean
    public EndpointParameterMapper endpointParameterMapper() {
        final Map<String, List<String>> endpointParameters = new HashMap<>();

        // Check account balance endpoint
        endpointParameters.put("/api/accounts/balance", List.of("accountID"));

        // Get transactions endpoint
        endpointParameters.put("/api/accounts/transactions", List.of("accountID"));

        // Payout endpoint
        endpointParameters.put("/api/accounts/payout", List.of("recipientAccountId","amount","senderAccountId"));

        // Recovery endpoint
        endpointParameters.put("/api/accounts/recovery", List.of("accountId"));

        // Top-up endpoint
        endpointParameters.put("/api/accounts/agent/topup", List.of("accountId", "amount"));

        // withdraw endpoint
        endpointParameters.put("/api/accounts/withdraw", List.of("senderAccountId","amount", "recipientAccountId"));


        return EndpointParameterMapper.builder()
                .endpointParameters(endpointParameters)
                .build();
    }
}