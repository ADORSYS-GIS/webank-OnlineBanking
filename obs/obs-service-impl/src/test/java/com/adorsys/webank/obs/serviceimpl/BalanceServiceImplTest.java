package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.BalanceRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String result = balanceService.getBalance(request);

        // Assert
        assertEquals("1000", result);
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
        String result = balanceService.getBalance(request);

        // Assert
        assertEquals("Balance empty", result);
    }

    @Test
    void testGetBalance_WithNullAccountDetails() {
        // Arrange
        BalanceRequest request = new BalanceRequest();
        request.setAccountID("12345");

        when(bankAccountService.getAccountDetailsById(anyString(), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(null);

        // Act
        String result = balanceService.getBalance(request);

        // Assert
        assertEquals("Balance empty", result);
    }
}
