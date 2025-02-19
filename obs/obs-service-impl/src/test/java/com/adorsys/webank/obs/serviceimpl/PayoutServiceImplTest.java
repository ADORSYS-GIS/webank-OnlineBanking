package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import de.adorsys.webank.bank.api.domain.AmountBO;
import de.adorsys.webank.bank.api.domain.BalanceBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PayoutServiceImplTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private JwtCertValidator jwtCertValidator;
    private ECKey serverKeyPair;

    @InjectMocks
    private PayoutServiceImpl payoutService;
    @BeforeEach
    void setUp() throws Exception {
        serverKeyPair = new ECKeyGenerator(Curve.P_256).keyID("server-key").generate();
        injectField("SERVER_PRIVATE_KEY_JSON", serverKeyPair.toJSONString());
        injectField("SERVER_PUBLIC_KEY_JSON", serverKeyPair.toPublicJWK().toJSONString());
        injectField("issuer", "https://webank.com");
        injectField("expirationTimeMs", 60000L);
    }

    private void injectField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = PayoutServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(payoutService, value);
    }

    // Helper method to create a basic PayoutRequest
    private PayoutRequest createPayoutRequest(String accountId, String otherAccountId, String amount) {
        PayoutRequest request = new PayoutRequest();
        request.setAccountID(accountId);
        request.setOtherAccountID(otherAccountId);
        request.setAmount(amount);
        return request;
    }

    /**
     * Instead of defining our own dummy classes for the balance chain,
     * we create mocks for the BalanceBO and AmountBO so that:
     *    balance.getAmount().getAmount() returns the given BigDecimal value.
     */
    private BankAccountDetailsBO createAccountDetails(BigDecimal balanceAmount) {
        BankAccountDetailsBO details = new BankAccountDetailsBO();
        // Create a mock for the balance object (of type BalanceBO)
        BalanceBO balance = mock(BalanceBO.class);
        // Create a mock for the amount object (of type AmountBO)
        AmountBO amount = mock(AmountBO.class);
        when(amount.getAmount()).thenReturn(balanceAmount);
        when(balance.getAmount()).thenReturn(amount);
        details.setBalances(Collections.singletonList(balance));
        return details;
    }

    @Test
    public void testPayoutInvalidJwt() {
        String invalidJwt = "invalidJwt";
        PayoutRequest request = createPayoutRequest("acc1", "acc2", "100");
        when(jwtCertValidator.validateJWT(invalidJwt)).thenReturn(false);

        String result = payoutService.payout(request, invalidJwt);
        assertEquals("Invalid certificate or JWT. Payout Request failed", result);
    }

    @Test
    public void testPayoutInvalidAmountFormat() {
        String validJwt = "validJwt";
        PayoutRequest request = createPayoutRequest("acc1", "acc2", "abc");
        when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

        String result = payoutService.payout(request, validJwt);
        assertEquals("Invalid amount format: abc", result);
    }

    @Test
    public void testPayoutNonPositiveAmount() {
        String validJwt = "validJwt";
        // Try with zero
        PayoutRequest request = createPayoutRequest("acc1", "acc2", "0");
        when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

        String result = payoutService.payout(request, validJwt);
        assertEquals("Amount must be a positive number", result);
    }

    @Test
    public void testPayoutUnableToRetrieveBalance() {
        String validJwt = "validJwt";
        PayoutRequest request = createPayoutRequest("acc1", "acc2", "100");
        when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

        // Simulate failure to retrieve account details (null returned)
        when(bankAccountService.getAccountDetailsById(eq("acc1"), any(LocalDateTime.class), eq(true)))
                .thenReturn(null);

        String result = payoutService.payout(request, validJwt);
        assertEquals("Unable to retrieve balance for the source account", result);
    }

    @Test
    public void testPayoutInsufficientBalance() {
        String validJwt = "validJwt";
        PayoutRequest request = createPayoutRequest("acc1", "acc2", "1000");
        when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

        // Create account details with a balance lower than the requested amount
        BankAccountDetailsBO details = createAccountDetails(new BigDecimal("500"));
        when(bankAccountService.getAccountDetailsById(eq("acc1"), any(LocalDateTime.class), eq(true)))
                .thenReturn(details);

        String result = payoutService.payout(request, validJwt);
        assertEquals("Insufficient balance. Current balance: 500 XAF", result);
    }

    @Test
    public void testPayoutAccountNotFound() {
        String validJwt = "validJwt";
        PayoutRequest request = createPayoutRequest("acc1", "acc2", "100");
        when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

        // Sufficient balance in source account
        BankAccountDetailsBO details = createAccountDetails(new BigDecimal("1000"));
        when(bankAccountService.getAccountDetailsById(eq("acc1"), any(LocalDateTime.class), eq(true)))
                .thenReturn(details);

        // Simulate account lookup: one account (acc1) is missing
        when(bankAccountService.getAccountById("acc1")).thenReturn(null);
        when(bankAccountService.getAccountById("acc2")).thenReturn(mock(BankAccountBO.class));

        String result = payoutService.payout(request, validJwt);
        assertEquals("One or both accounts not found", result);
    }

@Test
public void testPayoutTransactionSuccess() throws ParseException, JOSEException {
    String validJwt = "validJwt";
    String sourceAccountId = "acc1";
    String otherAccountId = "acc2";
    PayoutRequest request = createPayoutRequest(sourceAccountId, otherAccountId, "100");
    when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

    // Sufficient balance in source account
    BankAccountDetailsBO details = createAccountDetails(new BigDecimal("500"));
    when(bankAccountService.getAccountDetailsById(eq(sourceAccountId), any(LocalDateTime.class), eq(true)))
            .thenReturn(details);

    // Both accounts exist. Create dummy BankAccountBOs with IBANs.
    BankAccountBO account1 = mock(BankAccountBO.class);
    BankAccountBO account2 = mock(BankAccountBO.class);
    when(account1.getIban()).thenReturn("IBAN1");
    when(account2.getIban()).thenReturn("IBAN2");
    when(bankAccountService.getAccountById(sourceAccountId)).thenReturn(account1);
    when(bankAccountService.getAccountById(otherAccountId)).thenReturn(account2);

    // Simulate successful mock transaction (empty error map)
    when(transactionService.bookMockTransaction(any(List.class)))
            .thenReturn(Collections.emptyMap());

    String result = payoutService.payout(request, validJwt);

    // Assert that the result ends with " Success"
    assertTrue(result.endsWith(" Success"));

    // And also check that the JWT part is valid
    String jwtPart = result.substring(0, result.length() - " Success".length());
    SignedJWT signedJWT = SignedJWT.parse(jwtPart);
    assertEquals(JWSAlgorithm.ES256, signedJWT.getHeader().getAlgorithm());
}


    @Test
    public void testPayoutTransactionFailure() {
        String validJwt = "validJwt";
        String sourceAccountId = "acc1";
        String otherAccountId = "acc2";
        PayoutRequest request = createPayoutRequest(sourceAccountId, otherAccountId, "100");
        when(jwtCertValidator.validateJWT(validJwt)).thenReturn(true);

        // Sufficient balance in source account
        BankAccountDetailsBO details = createAccountDetails(new BigDecimal("500"));
        when(bankAccountService.getAccountDetailsById(eq(sourceAccountId), any(LocalDateTime.class), eq(true)))
                .thenReturn(details);

        // Both accounts exist.
        BankAccountBO account1 = mock(BankAccountBO.class);
        BankAccountBO account2 = mock(BankAccountBO.class);
        when(account1.getIban()).thenReturn("IBAN1");
        when(account2.getIban()).thenReturn("IBAN2");
        when(bankAccountService.getAccountById(sourceAccountId)).thenReturn(account1);
        when(bankAccountService.getAccountById(otherAccountId)).thenReturn(account2);

        // Simulate booking errors by returning a non-empty error map
        when(transactionService.bookMockTransaction(any(List.class)))
                .thenReturn(Collections.singletonMap("error", "booking error"));

        String result = payoutService.payout(request, validJwt);
        assertEquals("Transaction failed due to booking errors", result);
    }
    @Test
    void generateTransactionCert_ValidJwtStructure() throws ParseException {
        String cert = payoutService.generateTransactionCert("sender123", "receiver456", "1000");
        SignedJWT signedJWT = SignedJWT.parse(cert);
        assertEquals(JWSAlgorithm.ES256, signedJWT.getHeader().getAlgorithm());
        signedJWT.getHeader().getJWK();
    }

    @Test
    void generateTransactionCert_ValidClaims() throws ParseException {
        String cert = payoutService.generateTransactionCert("sender123", "receiver456", "1000");
        JWTClaimsSet claims = SignedJWT.parse(cert).getJWTClaimsSet();
        assertEquals("https://webank.com", claims.getIssuer());
        assertEquals("1000", claims.getClaim("amount"));
        assertEquals("sender123", claims.getClaim("from"));
        assertEquals("receiver456", claims.getClaim("to"));
        Assertions.assertNotNull(claims.getClaim("TranactionID"));
    }

    @Test
    void generateTransactionCert_ValidSignature() throws JOSEException, ParseException {
        String cert = payoutService.generateTransactionCert("sender123", "receiver456", "1000");
        SignedJWT signedJWT = SignedJWT.parse(cert);
        JWSVerifier verifier = new ECDSAVerifier(serverKeyPair.toPublicJWK());
        assertTrue(signedJWT.verify(verifier), "Signature validation failed");
    }
}
