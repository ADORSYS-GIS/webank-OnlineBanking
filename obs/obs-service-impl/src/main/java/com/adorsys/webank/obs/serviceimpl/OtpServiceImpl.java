package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.OtpServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpServiceApi {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    @Override
    public void receiveOtp(String otp) {
        // Add the business logic here for handling the OTP
        logger.info("Received OTP: {}", otp);

        // Example of processing the OTP (replace this with actual logic)
        if (otp != null && otp.length() == 6) {
            logger.info("OTP format is valid and being processed.");
            // Additional processing logic here
        } else {
            logger.error("Invalid OTP format received.");
            throw new IllegalArgumentException("Invalid OTP format");
        }
    }
}

