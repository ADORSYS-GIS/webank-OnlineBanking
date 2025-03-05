/**package com.adorsys.webank.obs.resource;


import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
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

        when(transService.getTrans(transRequest, jwtToken)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = transRest.getTrans("Bearer " + jwtToken, transRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(transService, times(1)).getTrans(transRequest, jwtToken);
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

        when(transService.getTrans(transRequest, jwtToken)).thenThrow(new RuntimeException("Service Error"));

        // Act
        ResponseEntity<String> response = transRest.getTrans("Bearer " + jwtToken, transRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(transService, times(1)).getTrans(transRequest, jwtToken);
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

}**/