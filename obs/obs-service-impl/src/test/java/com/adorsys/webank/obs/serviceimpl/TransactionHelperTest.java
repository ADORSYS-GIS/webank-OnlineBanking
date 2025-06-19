package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.InsufficientBalanceException;
import com.adorsys.webank.exception.InvalidAmountException;
import com.adorsys.webank.exception.TransactionException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.domain.TransactionDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.adorsys.webank.config.KeyLoader;

@ExtendWith(MockitoExtension.class)
class TransactionHelperTest {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHelperTest.class);
    private static final String VALID_ACCOUNT_ID = "123456";
    private static final String RECIPIENT_ACCOUNT_ID = "789012";
    private static final String VALID_AMOUNT = "100.50";
    private static final String INVALID_AMOUNT = "invalid";
    private static final String NEGATIVE_AMOUNT = "-50.00";
    private static final String CURRENCY_CODE = "XAF";

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private KeyLoader keyLoader;

    @InjectMocks
    private TransactionHelper transactionHelper;

    private ECKey ecJwk;
    private String validJwt;

    @BeforeEach
    void setUp() throws JOSEException {
        // Generate a test EC key
        ecJwk = new ECKeyGenerator(Curve.P_256)
                .keyID("test-key-id")
                .generate();

        ReflectionTestUtils.setField(transactionHelper, "issuer", "test-issuer");
        ReflectionTestUtils.setField(transactionHelper, "expirationTimeMs", 3600000L);

        // Generate a valid JWT for testing
        validJwt = generateValidJwt();
    }

    private String generateValidJwt() throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("test-issuer")
                .issueTime(new Date())
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(ecJwk.getKeyID())
                .build();

        SignedJWT jwt = new SignedJWT(header, claimsSet);
        jwt.sign(new ECDSASigner(ecJwk));
        return jwt.serialize();
    }

    @Test
    void testValidateAndProcessTransaction_Success() throws ParseException {
        // Arrange
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("1000.00")));
        when(bankAccountService.getAccountById(anyString()))
                .thenReturn(createMockBankAccount());
        when(transactionService.bookMockTransaction(any())).thenReturn(new HashMap<>());

        when(keyLoader.loadPrivateKey()).thenReturn(ecJwk);
        when(keyLoader.loadPublicKey()).thenReturn(ecJwk.toPublicJWK());

        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);

        // Assert
        assertTrue(result.endsWith("Success"));
        verify(transactionService).bookMockTransaction(any());
    }

    @Test
    void testValidateAndProcessTransaction_InvalidJwt() {
        // Act & Assert
        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, "invalid-jwt", logger);
        });

        assertEquals("Unable to retrieve balance for the source account: Unable to retrieve balance for the source account", 
            exception.getMessage());
    }

    @Test
    void testValidateAndProcessTransaction_InvalidAmountFormat() {
        // Act & Assert
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, INVALID_AMOUNT, validJwt, logger);
        });

        assertEquals("Invalid amount format: " + INVALID_AMOUNT, exception.getMessage());
    }

    @Test
    void testValidateAndProcessTransaction_NegativeAmount() {
        // Act & Assert
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, NEGATIVE_AMOUNT, validJwt, logger);
        });

        assertEquals("Amount must be a positive number", exception.getMessage());
    }

    @Test
    void testValidateAndProcessTransaction_InsufficientBalance() {
        // Arrange
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("50.00")));

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);
        });

        assertEquals("Insufficient balance. Current balance: 50.00 XAF", exception.getMessage());
    }

    @Test
    void testValidateAndProcessTransaction_AccountNotFound() {
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("1000.00")));
        when(bankAccountService.getAccountById(anyString())).thenReturn(null);

        // Act & Assert
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);
        });

        assertEquals("One or both accounts not found", exception.getMessage());
    }

    @Test
    void testValidateAndProcessTransaction_TransactionBookingFailed() {
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("1000.00")));
        when(bankAccountService.getAccountById(anyString()))
                .thenReturn(createMockBankAccount());
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "Booking failed");
        when(transactionService.bookMockTransaction(any())).thenReturn(errorMap);

        // Act & Assert
        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);
        });

        assertEquals("Transaction failed due to booking errors", exception.getMessage());
    }

    @Test
    void testGenerateTransactionCert_Success() throws ParseException {
        // Arrange
        when(bankAccountService.getTransactionsByDates(anyString(), any(), any()))
                .thenReturn(Collections.singletonList(createMockTransactionDetails()));
        when(keyLoader.loadPrivateKey()).thenReturn(ecJwk);
        when(keyLoader.loadPublicKey()).thenReturn(ecJwk.toPublicJWK());

        // Act
        String result = transactionHelper.generateTransactionCert(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("eyJ")); // JWT typically starts with "eyJ"
    }

    @Test
    void testGenerateTransactionCert_MissingPrivateKeyParameter() throws Exception {
        // Arrange
        when(keyLoader.loadPrivateKey()).thenReturn(null);

        // Act & Assert
        assertThrows(TransactionException.class, () -> {
            transactionHelper.generateTransactionCert(VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT);
        });
    }

    private BankAccountDetailsBO createMockAccountDetails(BigDecimal balance) {
        BankAccountDetailsBO details = new BankAccountDetailsBO();
        de.adorsys.webank.bank.api.domain.BalanceBO balanceBO = new de.adorsys.webank.bank.api.domain.BalanceBO();
        de.adorsys.webank.bank.api.domain.AmountBO amountBO = new de.adorsys.webank.bank.api.domain.AmountBO();
        amountBO.setAmount(balance);
        amountBO.setCurrency(Currency.getInstance(CURRENCY_CODE));
        balanceBO.setAmount(amountBO);
        details.setBalances(Collections.singletonList(balanceBO));
        return details;
    }

    private BankAccountBO createMockBankAccount() {
        BankAccountBO account = new BankAccountBO();
        account.setIban("TEST123456789");
        return account;
    }

    private TransactionDetailsBO createMockTransactionDetails() {
        TransactionDetailsBO details = new TransactionDetailsBO();
        details.setTransactionId("123");
        details.setBookingDate(LocalDate.now());
        return details;
    }
}