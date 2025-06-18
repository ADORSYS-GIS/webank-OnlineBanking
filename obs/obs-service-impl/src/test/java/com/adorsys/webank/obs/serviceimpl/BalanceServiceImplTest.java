package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.BalanceResponse;
import de.adorsys.webank.bank.api.domain.AmountBO;
import de.adorsys.webank.bank.api.domain.BalanceBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalance_WithValidBalance() {
        // Arrange
        BalanceRequest request = new BalanceRequest();
        request.setAccountID("12345");

        AmountBO amount = new AmountBO();
        amount.setCurrency(Currency.getInstance("XAF"));
        amount.setAmount(new BigDecimal("1000"));
        BalanceBO balance = new BalanceBO();
        balance.setAmount(amount);
        BankAccountDetailsBO accountDetails = new BankAccountDetailsBO();
        accountDetails.setBalances(List.of(balance));

        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(accountDetails);

        // Act
        BalanceResponse result = balanceService.getBalance(request);

        // Assert
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("1000", result.getBalance());
        assertEquals("12345", result.getAccountId());
        assertEquals("Balance retrieved successfully", result.getMessage());
    }

    @Test
    void testGetBalance_WithEmptyBalance() {
        // Arrange
        BalanceRequest request = new BalanceRequest();
        request.setAccountID("12345");

        BankAccountDetailsBO accountDetails = new BankAccountDetailsBO();
        accountDetails.setBalances(Collections.emptyList());

        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(accountDetails);

        // Act
        BalanceResponse result = balanceService.getBalance(request);

        // Assert
        assertEquals("ERROR", result.getStatus());
        assertEquals("Balance empty", result.getMessage());
        assertEquals("12345", result.getAccountId());
    }

    @Test
    void testGetBalance_WithNullAccountDetails() {
        // Arrange
        BalanceRequest request = new BalanceRequest();
        request.setAccountID("12345");

        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(null);

        // Act
        BalanceResponse result = balanceService.getBalance(request);

        // Assert
        assertEquals("ERROR", result.getStatus());
        assertEquals("Balance empty", result.getMessage());
        assertEquals("12345", result.getAccountId());
    }

    @Test
    void testGetBalanceFallback_WhenExceptionOccurs() {
        // Arrange
        BalanceRequest request = new BalanceRequest();
        request.setAccountID("12345");
        Exception exception = new RuntimeException("External service error");

        // Act
        BalanceResponse result = balanceService.getBalanceFallback(request, exception);

        // Assert
        assertEquals("SERVICE_UNAVAILABLE", result.getStatus());
        assertEquals("External service is temporarily unavailable", result.getMessage());
        assertEquals("12345", result.getAccountId());
        assertEquals("0", result.getBalance());
    }

    @Test
    void testGetBalance_WithServiceException() {
        // Arrange
        BalanceRequest request = new BalanceRequest();
        request.setAccountID("12345");

        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> balanceService.getBalance(request));
    }
}
