package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
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

        when(registrationService.registerAccount(registrationRequest, jwtToken)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken, registrationRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(registrationService, times(1)).registerAccount(registrationRequest, jwtToken);
    }

    @Test
    void testRegisterAccount_InvalidAuthorizationHeader_MissingBearer() {
        // Arrange
        String invalidAuthorizationHeader = "invalid-header";
        RegistrationRequest registrationRequest = new RegistrationRequest();

        // Act
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

        // Act
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

        when(registrationService.registerAccount(registrationRequest, jwtToken)).thenThrow(new RuntimeException("Service Error"));

        // Act
        ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken, registrationRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(registrationService, times(1)).registerAccount(registrationRequest, jwtToken);
    }


    @Test
    void testGetBalance_NullRequestBody() {
        // Arrange
        String jwtToken = "valid-jwt-token";

        // Act
        ResponseEntity<String> response = registrationResource.registerAccount("Bearer " + jwtToken, null);


        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body cannot be null.", response.getBody());
        verify(registrationService, never()).registerAccount(any(), any());
    }
}