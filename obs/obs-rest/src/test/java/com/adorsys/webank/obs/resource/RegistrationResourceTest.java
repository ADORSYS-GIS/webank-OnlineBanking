package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
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

class RegistrationResourceTest {

    @Mock
    private RegistrationServiceApi registrationService;

    @InjectMocks
    private RegistrationResource registrationResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterAccount_SuccessfulResponse() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        RegistrationRequest registrationRequest = new RegistrationRequest();
        String expectedResponse = "Account Registered Successfully";

        // Stub the static call so that it does nothing.
        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(any(), any(), any()))
                    .thenAnswer(invocation -> null);

            when(registrationService.registerAccount(registrationRequest, jwtToken)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken, registrationRequest);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
            verify(registrationService, times(1)).registerAccount(registrationRequest, jwtToken);
        }
    }

    @Test
    void testRegisterAccount_InvalidAuthorizationHeader_MissingBearer() {
        // Arrange
        String invalidAuthorizationHeader = "invalid-header";
        RegistrationRequest registrationRequest = new RegistrationRequest();

        // No need to stub static call as it should fail before it is invoked.
        ResponseEntity<String> response = registrationResource.registerAccount(invalidAuthorizationHeader, registrationRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(registrationService, never()).registerAccount(any(), any());
    }

    @Test
    void testRegisterAccount_InvalidAuthorizationHeader_NullHeader() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();

        ResponseEntity<String> response = registrationResource.registerAccount(null, registrationRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(registrationService, never()).registerAccount(any(), any());
    }

    @Test
    void testRegisterAccount_ServiceThrowsException() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        RegistrationRequest registrationRequest = new RegistrationRequest();

        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(any(), any(), any()))
                    .thenAnswer(invocation -> null);

            when(registrationService.registerAccount(registrationRequest, jwtToken)).thenThrow(new RuntimeException("Service Error"));

            // Act
            ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken, registrationRequest);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("An error occurred while processing the request.", response.getBody());
            verify(registrationService, times(1)).registerAccount(registrationRequest, jwtToken);
        }
    }

    @Test
    void testRegisterAccount_NullRequestBody() {
        // Arrange
        String jwtToken = "valid-jwt-token";

        ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body cannot be null.", response.getBody());
        verify(registrationService, never()).registerAccount(any(), any());
    }
}
