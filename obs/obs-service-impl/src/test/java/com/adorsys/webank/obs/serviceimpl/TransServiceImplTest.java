package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.dto.response.TransactionHistoryResponse;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransServiceImpl transService;

    private TransRequest transRequest;
    private static final String ACCOUNT_ID = "12345";
    private static final String TRANSACTION_ID = "txn-001";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100.50);

    @BeforeEach
    void setUp() {
        transRequest = new TransRequest();
        transRequest.setAccountID(ACCOUNT_ID);
    }

    @Test
    void testGetTrans_SuccessfulTransactionRetrieval() {
        // Setup
        BankAccountBO bankAccount = new BankAccountBO();
        when(bankAccountService.getAccountById(ACCOUNT_ID)).thenReturn(bankAccount);

        TransactionDetailsBO transaction = new TransactionDetailsBO();
        transaction.setTransactionId(TRANSACTION_ID);
        AmountBO amount = new AmountBO(Currency.getInstance("EUR"), AMOUNT);
        transaction.setTransactionAmount(amount);
        LocalDate bookingDate = LocalDate.now();
        transaction.setBookingDate(bookingDate);

        List<TransactionDetailsBO> transactions = List.of(transaction);
        when(bankAccountService.getTransactionsByDates(eq(ACCOUNT_ID), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        // Act
        TransactionHistoryResponse result = transService.getTrans(transRequest);

        // Assert
        assertEquals(TransactionHistoryResponse.TransactionStatus.SUCCESS, result.getStatus());
        assertEquals("Transaction history retrieved successfully", result.getMessage());
        assertNotNull(result.getTimestamp());
        
        // Verify JSON structure
        String data = result.getData();
        assertTrue(data.startsWith("[") && data.endsWith("]"));
        assertTrue(data.contains("\"id\": \"" + TRANSACTION_ID + "\""));
        assertTrue(data.contains("\"amount\": \"" + AMOUNT + "\""));
        assertTrue(data.contains("\"title\": \"Deposit\""));
        assertTrue(data.contains("\"date\": \"" + bookingDate + "\""));

        // Verify service calls
        verify(bankAccountService).getAccountById(ACCOUNT_ID);
        verify(bankAccountService).getTransactionsByDates(eq(ACCOUNT_ID), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetTrans_WithdrawalTransaction() {
        // Setup
        BankAccountBO bankAccount = new BankAccountBO();
        when(bankAccountService.getAccountById(ACCOUNT_ID)).thenReturn(bankAccount);

        TransactionDetailsBO transaction = new TransactionDetailsBO();
        transaction.setTransactionId(TRANSACTION_ID);
        AmountBO amount = new AmountBO(Currency.getInstance("EUR"), AMOUNT.negate());
        transaction.setTransactionAmount(amount);
        LocalDate bookingDate = LocalDate.now();
        transaction.setBookingDate(bookingDate);

        List<TransactionDetailsBO> transactions = List.of(transaction);
        when(bankAccountService.getTransactionsByDates(eq(ACCOUNT_ID), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        // Act
        TransactionHistoryResponse result = transService.getTrans(transRequest);

        // Assert
        assertEquals(TransactionHistoryResponse.TransactionStatus.SUCCESS, result.getStatus());
        
        // Verify JSON structure
        String data = result.getData();
        assertTrue(data.contains("\"title\": \"Withdrawal\""));
        assertTrue(data.contains("\"amount\": \"-" + AMOUNT + "\""));
    }

    @Test
    void testGetTrans_MultipleTransactions() {
        // Setup
        BankAccountBO bankAccount = new BankAccountBO();
        when(bankAccountService.getAccountById(ACCOUNT_ID)).thenReturn(bankAccount);

        TransactionDetailsBO deposit = new TransactionDetailsBO();
        deposit.setTransactionId("txn-001");
        deposit.setTransactionAmount(new AmountBO(Currency.getInstance("EUR"), AMOUNT));
        deposit.setBookingDate(LocalDate.now());

        TransactionDetailsBO withdrawal = new TransactionDetailsBO();
        withdrawal.setTransactionId("txn-002");
        withdrawal.setTransactionAmount(new AmountBO(Currency.getInstance("EUR"), AMOUNT.negate()));
        withdrawal.setBookingDate(LocalDate.now());

        List<TransactionDetailsBO> transactions = List.of(deposit, withdrawal);
        when(bankAccountService.getTransactionsByDates(eq(ACCOUNT_ID), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        // Act
        TransactionHistoryResponse result = transService.getTrans(transRequest);

        // Assert
        assertEquals(TransactionHistoryResponse.TransactionStatus.SUCCESS, result.getStatus());
        
        // Verify JSON structure
        String data = result.getData();
        assertTrue(data.contains("\"id\": \"txn-001\""));
        assertTrue(data.contains("\"id\": \"txn-002\""));
        assertTrue(data.contains("\"title\": \"Deposit\""));
        assertTrue(data.contains("\"title\": \"Withdrawal\""));
    }
    
    @Test
    void testGetTrans_AccountNotFound() {
        // Setup
        when(bankAccountService.getAccountById(ACCOUNT_ID)).thenReturn(null);

        // Act
        TransactionHistoryResponse result = transService.getTrans(transRequest);

        // Assert
        assertEquals(TransactionHistoryResponse.TransactionStatus.FAILED, result.getStatus());
        assertEquals("Bank account not found for ID: " + ACCOUNT_ID, result.getMessage());
        assertEquals("[]", result.getData());
        assertNotNull(result.getTimestamp());

        // Verify service call
        verify(bankAccountService).getAccountById(ACCOUNT_ID);
    }

    @Test
    void testGetTrans_NoTransactionsFound() {
        // Setup
        when(bankAccountService.getAccountById(ACCOUNT_ID)).thenReturn(new BankAccountBO());
        when(bankAccountService.getTransactionsByDates(eq(ACCOUNT_ID), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act
        TransactionHistoryResponse result = transService.getTrans(transRequest);

        // Assert
        assertEquals(TransactionHistoryResponse.TransactionStatus.SUCCESS, result.getStatus());
        assertEquals("Transaction history retrieved successfully", result.getMessage());
        assertEquals("[]", result.getData());
        assertNotNull(result.getTimestamp());

        // Verify service calls
        verify(bankAccountService).getAccountById(ACCOUNT_ID);
        verify(bankAccountService).getTransactionsByDates(eq(ACCOUNT_ID), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetTrans_ExceptionHandling() {
        // Setup
        when(bankAccountService.getAccountById(ACCOUNT_ID)).thenThrow(new RuntimeException("Database error"));

        // Act
        TransactionHistoryResponse result = transService.getTrans(transRequest);

        // Assert
        assertEquals(TransactionHistoryResponse.TransactionStatus.FAILED, result.getStatus());
        assertEquals("An error occurred while processing the request: Database error", result.getMessage());
        assertEquals("[]", result.getData());
        assertNotNull(result.getTimestamp());

        // Verify service call
        verify(bankAccountService).getAccountById(ACCOUNT_ID);
    }
}
