/*package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
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

        when(balanceService.getBalance(balanceRequest, jwtToken)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = balanceRest.getBalance("Bearer " + jwtToken, balanceRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(balanceService, times(1)).getBalance(balanceRequest, jwtToken);
    }

    @Test
    void testGetBalance_InvalidAuthorizationHeader_MissingBearer() {
        // Arrange
        String invalidAuthorizationHeader = "invalid-header";
        BalanceRequest balanceRequest = new BalanceRequest();

        // Act
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

        // Act
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

        when(balanceService.getBalance(balanceRequest, jwtToken)).thenThrow(new RuntimeException("Service Error"));

        // Act
        ResponseEntity<String> response = balanceRest.getBalance("Bearer " + jwtToken, balanceRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody());
        verify(balanceService, times(1)).getBalance(balanceRequest, jwtToken);
    }

    @Test
    void testGetBalance_NullRequestBody() {
        // Arrange
        String jwtToken = "valid-jwt-token";

        // Act
        ResponseEntity<String> response = balanceRest.getBalance("Bearer " + jwtToken, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body cannot be null.", response.getBody());
        verify(balanceService, never()).getBalance(any(), any());
    }

}**/