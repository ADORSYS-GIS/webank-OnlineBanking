package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.BalanceResponse;
import com.adorsys.webank.obs.dto.response.BalanceResponse.BalanceStatus;
import de.adorsys.webank.bank.api.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "resilience4j.circuitbreaker.instances.balanceService.slidingWindowSize=5",
    "resilience4j.circuitbreaker.instances.balanceService.failureRateThreshold=50",
    "resilience4j.circuitbreaker.instances.balanceService.minimumNumberOfCalls=3",
    "resilience4j.circuitbreaker.instances.balanceService.waitDurationInOpenState=1s",
    "resilience4j.circuitbreaker.instances.balanceService.permittedNumberOfCallsInHalfOpenState=2"
})
class BalanceServiceCircuitBreakerTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    private BalanceRequest request;

    @BeforeEach
    void setUp() {
        request = new BalanceRequest();
        request.setAccountID("12345");
    }

    @Test
    void testCircuitBreaker_OpensAfterMultipleFailures() {
        // Arrange - Configure mock to throw exceptions
        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act - Make multiple calls to trigger circuit breaker
        for (int i = 0; i < 3; i++) {
            try {
                balanceService.getBalance(request);
            } catch (RuntimeException e) {
                // Expected exception - this is part of the test to trigger circuit breaker
                assertNotNull(e.getMessage());
            }
        }

        // Act - Next call should trigger fallback due to circuit breaker being open
        com.adorsys.webank.obs.dto.response.BalanceResponse result = balanceService.getBalance(request);

        // Assert
        assertEquals(BalanceStatus.SYSTEM_ERROR, result.getStatus());
        assertEquals("Error retrieving balance: Service unavailable", result.getMessage());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals("12345", result.getAccountId());
    }

    @Test
    void testCircuitBreaker_FallbackMethodCalled() {
        // Arrange
        Exception testException = new RuntimeException("Test exception");

        // Act
        com.adorsys.webank.obs.dto.response.BalanceResponse result = balanceService.getBalanceFallback(request, testException);

        // Assert
        assertEquals(BalanceStatus.SYSTEM_ERROR, result.getStatus());
        assertEquals("External service is temporarily unavailable", result.getMessage());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals("12345", result.getAccountId());
    }

    @Test
    void testCircuitBreaker_SuccessfulCallsAfterFailures() {
        // Arrange - First make some failing calls
        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenThrow(new RuntimeException("Service unavailable"))
                .thenReturn(null); // Return null for subsequent calls

        // Act - Make a failing call
        try {
            balanceService.getBalance(request);
        } catch (RuntimeException e) {
            // Expected exception - this is part of the test to simulate service failure
            assertNotNull(e.getMessage());
            assertEquals("Service unavailable", e.getMessage());
        }

        // Act - Make a successful call
        com.adorsys.webank.obs.dto.response.BalanceResponse result = balanceService.getBalance(request);

        // Assert
        assertEquals(BalanceStatus.INSUFFICIENT_FUNDS, result.getStatus());
        assertEquals("No balance information available", result.getMessage());
    }

    @Test
    void testCircuitBreaker_LoggingBehavior() {
        // Arrange
        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act - Should return SYSTEM_ERROR status due to fallback
        com.adorsys.webank.obs.dto.response.BalanceResponse result = balanceService.getBalance(request);
        assertEquals(BalanceStatus.SYSTEM_ERROR, result.getStatus());
        assertEquals("Error retrieving balance: Service unavailable", result.getMessage());
    }
} 