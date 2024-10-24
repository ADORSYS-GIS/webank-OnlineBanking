package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.OtpServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpServiceApi {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    @Override
    public String receiveOtp(String otp) {
        if (otp == null || otp.length() != 6) {
            logger.error("Invalid OTP format received.");
            throw new IllegalArgumentException("Invalid OTP format");
        }

        // Add actual OTP processing logic here
        logger.info("OTP successfully processed: {}", otp);
        return "OTP successfully received";
    }
}


