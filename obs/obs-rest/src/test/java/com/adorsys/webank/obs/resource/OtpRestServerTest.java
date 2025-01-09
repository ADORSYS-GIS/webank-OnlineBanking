//package com.adorsys.webank.obs.resource;
//
//import com.adorsys.webank.obs.dto.OtpRequest;
//import com.adorsys.webank.obs.service.OtpServiceApi;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class OtpRestServerTest {
//
//    @InjectMocks
//    private OtpRestServer otpRestServer; // Class under test
//
//    @Mock
//    private OtpServiceApi otpServiceApi; // Mocked dependency
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void receiveOtp_ShouldReturnSuccessfulResponse() {
//        // Arrange
//        OtpRequest otpRequest = new OtpRequest();
//        otpRequest.setOtp("123456");
//
//        String expectedResponse = "OTP is valid";
//        when(otpServiceApi.receiveOtp(otpRequest.getOtp())).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity<?> responseEntity = otpRestServer.receiveOtp(otpRequest);
//
//        // Assert
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        assertEquals(Map.of("message", "OTP is valid"), responseEntity.getBody());
//    }
//
//    @Test
//    void receiveOtp_ShouldReturnBadRequestForInvalidOtp() {
//        // Arrange
//        OtpRequest otpRequest = new OtpRequest();
//        otpRequest.setOtp("invalid");
//
//        String expectedErrorMessage = "Invalid OTP";
//        when(otpServiceApi.receiveOtp(otpRequest.getOtp())).thenThrow(new IllegalArgumentException(expectedErrorMessage));
//
//        // Act
//        ResponseEntity<?> responseEntity = otpRestServer.receiveOtp(otpRequest);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertEquals(Map.of("message", expectedErrorMessage), responseEntity.getBody());
//    }
//}
//
