package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.TransRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransServiceImpl transService;

    private TransRequest transRequest;

    @BeforeEach
    void setUp() {
        transRequest = new TransRequest();
        transRequest.setAccountID("12345");
    }

    @Test
    void testGetTrans_SuccessfulTransactionRetrieval() {
        // Return a non-null BankAccountBO (details not important for this test)
        BankAccountBO bankAccount = new BankAccountBO();
        when(bankAccountService.getAccountById("12345")).thenReturn(bankAccount);

        // Create a dummy TransactionDetailsBO with an AmountBO value
        TransactionDetailsBO transaction = new TransactionDetailsBO();
        transaction.setTransactionId("txn-001");
        // Create an AmountBO instance with EUR and an amount of 100.50
        AmountBO amount = new AmountBO(Currency.getInstance("EUR"), BigDecimal.valueOf(100.50));
        transaction.setTransactionAmount(amount);
        transaction.setBookingDate(LocalDate.from(LocalDateTime.now())); // Make sure to set the booking date

        List<TransactionDetailsBO> transactions = List.of(transaction);
        when(bankAccountService.getTransactionsByDates(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        // Act
        String result = transService.getTrans(transRequest);

        // Assert
        String expected = "[\n" +
                "{\n" +
                "  \"id\": \"txn-001\",\n" +
                "  \"date\": \"" + transaction.getBookingDate().toString() + "\",\n" +
                "  \"amount\": \"" + transaction.getTransactionAmount().getAmount() + "\",\n" +
                "  \"title\": \"Deposit\"\n" +
                "}\n" +
                "]";
        assertEquals(expected, result);
    }
    
    @Test
    void testGetTrans_AccountNotFound() {
        when(bankAccountService.getAccountById("12345")).thenReturn(null);

        // Act
        String result = transService.getTrans(transRequest);

        // Assert
        assertEquals("Bank account not found for ID: 12345", result);
    }

    @Test
    void testGetTrans_NoTransactionsFound() {
        when(bankAccountService.getAccountById("12345")).thenReturn(new BankAccountBO());
        when(bankAccountService.getTransactionsByDates(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act
        String result = transService.getTrans(transRequest);

        // Assert
        assertEquals("No transactions found for the given account and date range.", result);
    }

    @Test
    void testGetTrans_ExceptionHandling() {
        when(bankAccountService.getAccountById("12345")).thenThrow(new RuntimeException("Database error"));

        // Act
        String result = transService.getTrans(transRequest);

        // Assert
        assertEquals("An error occurred while processing the request: Database error", result);
    }
}
