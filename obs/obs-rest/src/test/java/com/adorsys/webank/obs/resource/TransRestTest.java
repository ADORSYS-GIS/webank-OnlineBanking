package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.TransServiceApi;
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

class TransRestTest {

    @Mock
    private TransServiceApi transService;

    @InjectMocks
    private TransRest transRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrans_SuccessfulResponse() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        TransRequest transRequest = new TransRequest();
        String expectedResponse = "Transaction Retrieved Successfully";

        // Stub the static call for JWT validation
        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(any(String.class), any()))
                    .thenAnswer(invocation -> null);

            when(transService.getTrans(transRequest, jwtToken)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<String> response = transRest.getTrans("Bearer " + jwtToken, transRequest);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
            verify(transService, times(1)).getTrans(transRequest, jwtToken);
        }
    }

    @Test
    void testGetTrans_InvalidAuthorizationHeader_MissingBearer() {
        // Arrange
        String invalidAuthorizationHeader = "invalid-header";
        TransRequest transRequest = new TransRequest();

        // Act
        ResponseEntity<String> response = transRest.getTrans(invalidAuthorizationHeader, transRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(transService, never()).getTrans(any(), any());
    }

    @Test
    void testGetTrans_InvalidAuthorizationHeader_NullHeader() {
        // Arrange
        TransRequest transRequest = new TransRequest();

        // Act
        ResponseEntity<String> response = transRest.getTrans(null, transRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(transService, never()).getTrans(any(), any());
    }

    @Test
    void testGetTrans_ServiceThrowsException() {
        // Arrange
        String jwtToken = "valid-jwt-token";
        TransRequest transRequest = new TransRequest();

        // Stub the static call for JWT validation
        try (MockedStatic<JwtValidator> jwtValidatorMock = mockStatic(JwtValidator.class)) {
            jwtValidatorMock.when(() -> JwtValidator.validateAndExtract(any(String.class), any()))
                    .thenAnswer(invocation -> null);

            when(transService.getTrans(transRequest, jwtToken)).thenThrow(new RuntimeException("Service Error"));

            // Act
            ResponseEntity<String> response = transRest.getTrans("Bearer " + jwtToken, transRequest);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("An error occurred while processing the request.", response.getBody());
            verify(transService, times(1)).getTrans(transRequest, jwtToken);
        }
    }

    @Test
    void testGetTrans_NullRequestBody() {
        // Arrange
        String jwtToken = "valid-jwt-token";

        // Act
        ResponseEntity<String> response = transRest.getTrans("Bearer " + jwtToken, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body cannot be null.", response.getBody());
        verify(transService, never()).getTrans(any(), any());
    }
}
