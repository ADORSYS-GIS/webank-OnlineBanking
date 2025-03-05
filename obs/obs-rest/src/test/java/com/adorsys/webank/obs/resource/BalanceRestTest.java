package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.BalanceServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BalanceRestTest {

    @Mock
    private BalanceServiceApi balanceService;

    @InjectMocks
    private BalanceRest balanceRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalance_SuccessfulResponse() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        BalanceRequest balanceRequest = new BalanceRequest();
        String expectedResponse = "Balance Retrieved Successfully";

        // Stub the static JwtValidator.validateAndExtract to do nothing.
        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(any(), any()))
                    .thenAnswer(invocation -> null);

            when(balanceService.getBalance(balanceRequest, jwtToken)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<String> response = balanceRest.getBalance("Bearer " + jwtToken, balanceRequest);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
            verify(balanceService, times(1)).getBalance(balanceRequest, jwtToken);
        }
    }

    @Test
    void testGetBalance_InvalidAuthorizationHeader_MissingBearer() {
        // Arrange
        String invalidAuthorizationHeader = "invalid-header";
        BalanceRequest balanceRequest = new BalanceRequest();

        // No need to mock the static call since the error happens before JwtValidator is invoked.
        ResponseEntity<String> response = balanceRest.getBalance(invalidAuthorizationHeader, balanceRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(balanceService, never()).getBalance(any(), any());
    }

    @Test
    void testGetBalance_InvalidAuthorizationHeader_NullHeader() {
        // Arrange
        BalanceRequest balanceRequest = new BalanceRequest();

        ResponseEntity<String> response = balanceRest.getBalance(null, balanceRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(balanceService, never()).getBalance(any(), any());
    }

    @Test
    void testGetBalance_ServiceThrowsException() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        BalanceRequest balanceRequest = new BalanceRequest();

        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(any(), any()))
                    .thenAnswer(invocation -> null);

            when(balanceService.getBalance(balanceRequest, jwtToken)).thenThrow(new RuntimeException("Service Error"));

            // Act
            ResponseEntity<String> response = balanceRest.getBalance("Bearer " + jwtToken, balanceRequest);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("An error occurred while processing the request.", response.getBody());
            verify(balanceService, times(1)).getBalance(balanceRequest, jwtToken);
        }
    }

    @Test
    void testGetBalance_NullRequestBody() {
        // Arrange
        String jwtToken = "valid-jwt-token";

        ResponseEntity<String> response = balanceRest.getBalance("Bearer " + jwtToken, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body cannot be null.", response.getBody());
        verify(balanceService, never()).getBalance(any(), any());
    }
}
