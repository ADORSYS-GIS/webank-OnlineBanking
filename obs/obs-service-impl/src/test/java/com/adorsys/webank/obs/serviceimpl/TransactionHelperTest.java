package com.adorsys.webank.obs.serviceimpl;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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


    @InjectMocks
    private TransactionHelper transactionHelper;

    private ECKey ecJwk;
    private String validJwt;

    @BeforeEach
    void setUp() throws JOSEException {
        // Generate test EC key
        ecJwk = new ECKeyGenerator(Curve.P_256)
                .keyID("test-key-id")
                .generate();

        // Set up test configuration
        ReflectionTestUtils.setField(transactionHelper, "serverPrivateKeyJson", ecJwk.toJSONString());
        ReflectionTestUtils.setField(transactionHelper, "serverPublicKeyJson", ecJwk.toPublicJWK().toJSONString());
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
    void testValidateAndProcessTransaction_Success() {
        // Arrange
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("1000.00")));
        when(bankAccountService.getAccountById(anyString()))
                .thenReturn(createMockBankAccount());
        when(transactionService.bookMockTransaction(any())).thenReturn(new HashMap<>());

        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);

        // Assert
        assertTrue(result.endsWith("Success"));
        verify(transactionService).bookMockTransaction(any());
    }

    @Test
    void testValidateAndProcessTransaction_InvalidJwt() {
        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, "invalid-jwt", logger);

        // Assert
        assertEquals("Invalid certificate or JWT. Payout Request failed", result);
    }

    @Test
    void testValidateAndProcessTransaction_InvalidAmountFormat() {
        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, INVALID_AMOUNT, validJwt, logger);

        // Assert
        assertEquals("Invalid amount format: " + INVALID_AMOUNT, result);
    }

    @Test
    void testValidateAndProcessTransaction_NegativeAmount() {

        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, NEGATIVE_AMOUNT, validJwt, logger);

        // Assert
        assertEquals("Amount must be a positive number", result);
    }

    @Test
    void testValidateAndProcessTransaction_InsufficientBalance() {
        // Arrange
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("50.00")));

        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);

        // Assert
        assertEquals("Insufficient balance. Current balance: 50.00 XAF", result);
    }

    @Test
    void testValidateAndProcessTransaction_AccountNotFound() {
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean()))
                .thenReturn(createMockAccountDetails(new BigDecimal("1000.00")));
        when(bankAccountService.getAccountById(anyString())).thenReturn(null);

        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);

        // Assert
        assertEquals("One or both accounts not found", result);
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

        // Act
        String result = transactionHelper.validateAndProcessTransaction(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT, validJwt, logger);

        // Assert
        assertEquals("Transaction failed due to booking errors", result);
    }

    @Test
    void testGenerateTransactionCert_Success() {
        // Arrange
        when(bankAccountService.getTransactionsByDates(anyString(), any(), any()))
                .thenReturn(Collections.singletonList(createMockTransactionDetails()));

        // Act
        String result = transactionHelper.generateTransactionCert(
                VALID_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, VALID_AMOUNT);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("eyJ")); // JWT typically starts with "eyJ"
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