package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import de.adorsys.webank.bank.api.domain.AmountBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.TransactionDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private JwtCertValidator jwtCertValidator;

    @InjectMocks
    private TransServiceImpl transService;

    private TransRequest transRequest;
    private String accountCertificateJwt;

    @BeforeEach
    void setUp() {
        transRequest = new TransRequest();
        transRequest.setAccountID("12345");
        // For this test we assume the date range is defined internally as one month ago to now.
        accountCertificateJwt = "valid-jwt";
    }

    @Test
    void testGetTrans_SuccessfulTransactionRetrieval() {
        // Arrange
        when(jwtCertValidator.validateJWT(accountCertificateJwt)).thenReturn(true);

        // Return a non-null BankAccountBO (details not important for this test)
        BankAccountBO bankAccount = new BankAccountBO();
        when(bankAccountService.getAccountById("12345")).thenReturn(bankAccount);

        // Create a dummy TransactionDetailsBO with an AmountBO value
        TransactionDetailsBO transaction = new TransactionDetailsBO();
        transaction.setTransactionId("txn-001");
        // Create an AmountBO instance with EUR and an amount of 100.50
        AmountBO amount = new AmountBO(Currency.getInstance("EUR"), BigDecimal.valueOf(100.50));
        transaction.setTransactionAmount(amount);

        List<TransactionDetailsBO> transactions = List.of(transaction);
        when(bankAccountService.getTransactionsByDates(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        // Act
        String result = transService.getTrans(transRequest, accountCertificateJwt);

        // Assert
        String expected = "Transaction ID: txn-001, Informations: null, Amount: 100.5";
        assertEquals(expected, result);
    }

    @Test
    void testGetTrans_InvalidJWT() {
        // Arrange
        when(jwtCertValidator.validateJWT(accountCertificateJwt)).thenReturn(false);

        // Act
        String result = transService.getTrans(transRequest, accountCertificateJwt);

        // Assert
        assertEquals("Invalid certificate or JWT. Transaction retrieval failed.", result);
    }

    @Test
    void testGetTrans_AccountNotFound() {
        // Arrange
        when(jwtCertValidator.validateJWT(accountCertificateJwt)).thenReturn(true);
        when(bankAccountService.getAccountById("12345")).thenReturn(null);

        // Act
        String result = transService.getTrans(transRequest, accountCertificateJwt);

        // Assert
        assertEquals("Bank account not found for ID: 12345", result);
    }

    @Test
    void testGetTrans_NoTransactionsFound() {
        // Arrange
        when(jwtCertValidator.validateJWT(accountCertificateJwt)).thenReturn(true);
        when(bankAccountService.getAccountById("12345")).thenReturn(new BankAccountBO());
        when(bankAccountService.getTransactionsByDates(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act
        String result = transService.getTrans(transRequest, accountCertificateJwt);

        // Assert
        assertEquals("No transactions found for the given account and date range.", result);
    }

    @Test
    void testGetTrans_ExceptionHandling() {
        // Arrange
        when(jwtCertValidator.validateJWT(accountCertificateJwt)).thenReturn(true);
        when(bankAccountService.getAccountById("12345")).thenThrow(new RuntimeException("Database error"));

        // Act
        String result = transService.getTrans(transRequest, accountCertificateJwt);

        // Assert
        assertEquals("An error occurred while processing the request: Database error", result);
    }
}
