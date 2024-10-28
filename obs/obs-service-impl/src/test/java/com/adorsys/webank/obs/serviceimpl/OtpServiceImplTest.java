package com.adorsys.webank.obs.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class OtpServiceImplTest {

    @InjectMocks
    private OtpServiceImpl otpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveOTP_valid() {
        String otp = "123456";

        // Call the method
        String result = otpService.receiveOtp(otp);

        // Assertions
        assertNotNull(result);
        assertEquals("OTP successfully received", result);
    }

    @Test
    void testReceiveOTP_invalid() {
        String otp = null;

        // Call the method
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            otpService.receiveOtp(otp);
        });

        // Verify exception message
        assertEquals("Invalid OTP format", exception.getMessage());
    }
}

