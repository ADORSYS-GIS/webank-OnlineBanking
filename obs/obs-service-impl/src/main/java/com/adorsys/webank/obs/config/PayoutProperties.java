package com.adorsys.webank.obs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "payout")
public class PayoutProperties {
    private double kycCheckThreshold;
}
