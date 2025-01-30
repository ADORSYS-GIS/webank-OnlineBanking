package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.config.PhoneNumberCache;
import com.adorsys.webank.service.OtpServiceApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObsOtpServiceImplTest {

    private static final String TEST_PHONE = "+123456789";
    private static final String TEST_PUBKEY = "public-key-123";
    private static final String TEST_OTP = "123456";
    private static final String TEST_HASH = "hash-123";

    @Mock
    private OtpServiceApi otpServiceApi;

    @Mock
    private PhoneNumberCache phoneNumberCache;

    @InjectMocks
    private ObsOtpServiceImpl obsOtpService;

    @Test
    void sendOtp_WhenNotCached_ShouldAddToCacheAndReturnResult() {
        when(phoneNumberCache.isInCache(anyString())).thenReturn(false);
        when(otpServiceApi.sendOtp(anyString(), anyString())).thenReturn("success");

        String result = obsOtpService.sendOtp(TEST_PHONE, TEST_PUBKEY);

        assertEquals("success", result);
        verify(phoneNumberCache).addToCache(TEST_PHONE);
        verify(otpServiceApi).sendOtp(TEST_PHONE, TEST_PUBKEY);
    }

    @Test
    void sendOtp_WhenAlreadyCached_ShouldThrowException() {
        when(phoneNumberCache.isInCache(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> obsOtpService.sendOtp(TEST_PHONE, TEST_PUBKEY)
        );

        assertEquals("OTP already sent recently for this phone number.", exception.getMessage());
        verify(phoneNumberCache, never()).addToCache(anyString());
        verify(otpServiceApi, never()).sendOtp(anyString(), anyString());
    }

    @Test
    void validateOtp_WhenSuccessful_ShouldRemoveFromCache() {
        String successResponse = "Certificate:valid";
        when(otpServiceApi.validateOtp(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(successResponse);

        String result = obsOtpService.validateOtp(TEST_PHONE, TEST_PUBKEY, TEST_OTP, TEST_HASH);

        assertEquals(successResponse, result);
        verify(phoneNumberCache).removeFromCache(TEST_PHONE);
    }

    @Test
    void validateOtp_WhenFailed_ShouldNotRemoveFromCache() {
        String failureResponse = "invalid";
        when(otpServiceApi.validateOtp(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(failureResponse);

        String result = obsOtpService.validateOtp(TEST_PHONE, TEST_PUBKEY, TEST_OTP, TEST_HASH);

        assertEquals("OTP validation failed for phone number: " + TEST_PHONE, result);
        verify(phoneNumberCache, never()).removeFromCache(anyString());
    }
}