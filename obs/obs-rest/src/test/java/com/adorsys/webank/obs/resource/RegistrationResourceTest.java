package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import com.nimbusds.jose.jwk.JWK;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegistrationResourceTest {

    @Mock
    private RegistrationServiceApi registrationService;

    @Mock
    private JWK mockJwk;

    @InjectMocks
    private RegistrationResource registrationResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockJwk.toJSONString()).thenReturn("mock-public-key");
    }

    @Test
    void registerAccountSuccessfulResponse() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        String expectedResponse = "Account Registered Successfully";

        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(eq(jwtToken)))
                    .thenReturn(mockJwk);

            when(registrationService.registerAccount(eq("mock-public-key"), eq(jwtToken)))
                    .thenReturn(expectedResponse);

            // Act
            ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
            verify(registrationService, times(1)).registerAccount(eq("mock-public-key"), eq(jwtToken));
        }
    }

    @Test
    void registerAccountInvalidAuthorizationHeaderMissingBearer() {
        // Arrange
        String invalidAuthorizationHeader = "invalid-header";

        // Act
        ResponseEntity<String> response = registrationResource.registerAccount(invalidAuthorizationHeader);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(registrationService, never()).registerAccount(any(), any());
    }

    @Test
    void registerAccountInvalidAuthorizationHeaderNullHeader() {
        // Act
        ResponseEntity<String> response = registrationResource.registerAccount(null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(registrationService, never()).registerAccount(any(), any());
    }

    @Test
    void registerAccountServiceThrowsException() {
        // Arrange
        String jwtToken = "valid-jwt-token";

        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(eq(jwtToken)))
                    .thenReturn(mockJwk);

            when(registrationService.registerAccount(eq("mock-public-key"), eq(jwtToken)))
                    .thenThrow(new RuntimeException("Service Error"));

            // Act
            ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("An error occurred while processing the request.", response.getBody());
            verify(registrationService, times(1)).registerAccount(eq("mock-public-key"), eq(jwtToken));
        }
    }
}
