package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.domain.MockBookingDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {
    private static final Logger LOG = LoggerFactory.getLogger(PayoutServiceImpl.class);

    @Value("${server.private.key.json}")
    private String SERVER_PRIVATE_KEY_JSON;

    @Value("${server.public.key.json}")
    private String SERVER_PUBLIC_KEY_JSON;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration-time-ms}")
    private Long expirationTimeMs;
    private static final String CURRENCY_CODE = "XAF";

    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;

    public PayoutServiceImpl(TransactionService transactionService, BankAccountService bankAccountService, JwtCertValidator jwtCertValidator) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.jwtCertValidator = jwtCertValidator;
    }

    @Override
    public String payout(PayoutRequest payoutRequest, String accountCertificateJwt) {
        if (!isValidJwt(accountCertificateJwt)) {
            return "Invalid certificate or JWT. Payout Request failed";
        }

        BigDecimal amountToSend = parseAmount(payoutRequest.getAmount());
        if (amountToSend == null) {
            return "Invalid amount format: " + payoutRequest.getAmount();
        }
        if (amountToSend.compareTo(BigDecimal.ZERO) <= 0) {
            return "Amount must be a positive number";
        }
        String accountId = payoutRequest.getAccountID();

        BigDecimal currentBalance = getCurrentBalance(accountId);
        if (currentBalance == null) {
            return "Unable to retrieve balance for the source account";
        }

        if (currentBalance.compareTo(amountToSend) < 0) {
            return "Insufficient balance. Current balance: " + currentBalance + " XAF";
        }

        String otherAccountId = payoutRequest.getOtherAccountID();
        return processTransaction(accountId, otherAccountId, amountToSend);
    }

    private boolean isValidJwt(String accountCertificateJwt) {
        try {
            boolean isValid = jwtCertValidator.validateJWT(accountCertificateJwt);
            LOG.info("The AccountCert is: {}", accountCertificateJwt);
            return isValid;
        } catch (Exception e) {
            LOG.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    private BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            LOG.error("Invalid amount format: {}", amount);
            return null;
        }
    }

    private BigDecimal getCurrentBalance(String accountId) {
        try {
            BankAccountDetailsBO accountDetails = bankAccountService.getAccountDetailsById(accountId, LocalDateTime.now(), true);
            if (accountDetails == null || accountDetails.getBalances().isEmpty()) {
                return null;
            }
            return accountDetails.getBalances().stream()
                    .findFirst()
                    .map(balance -> balance.getAmount().getAmount())
                    .orElse(null);
        } catch (Exception e) {
            LOG.error("Failed to retrieve account balance: {}", e.getMessage());
            return null;
        }
    }

    private String processTransaction(String accountId, String otherAccountId, BigDecimal amount) {
        BankAccountBO account1 = bankAccountService.getAccountById(accountId);
        BankAccountBO account2 = bankAccountService.getAccountById(otherAccountId);

        if (account1 == null || account2 == null) {
            return "One or both accounts not found";
        }

        MockBookingDetailsBO mockTransaction = createMockTransaction(account1.getIban(), account2.getIban(), amount);

        List<MockBookingDetailsBO> transactions = Collections.singletonList(mockTransaction);
        Map<String, String> errorMap = transactionService.bookMockTransaction(transactions);

        if (errorMap.isEmpty()) {
            LOG.info("Mock transaction for account {} booked successfully.", accountId);
        } else {
            LOG.error("Errors occurred while booking transaction(s): {}", errorMap);
            return "Transaction failed due to booking errors";
        }
        String transactionCert = generateTransactionCert(accountId, otherAccountId, String.valueOf(amount));

        return transactionCert +  " Success";
    }

    private MockBookingDetailsBO createMockTransaction(String iban1, String iban2, BigDecimal amount) {
        MockBookingDetailsBO mockTransaction = new MockBookingDetailsBO();
        mockTransaction.setUserAccount(iban1);
        mockTransaction.setOtherAccount(iban2);
        mockTransaction.setAmount(amount);
        mockTransaction.setCurrency(Currency.getInstance(CURRENCY_CODE));
        mockTransaction.setBookingDate(LocalDate.now());
        mockTransaction.setValueDate(LocalDate.now().plusDays(1));
        mockTransaction.setCrDrName("Test User");
        mockTransaction.setRemittance("Payment for testing purposes");
        return mockTransaction;
    }

    public String generateTransactionCert(String senderAccount, String receiverAccount, String amount) {
        try {

            // Parse server's private key from JWK JSON
            ECKey serverPrivateKey = (ECKey) JWK.parse(SERVER_PRIVATE_KEY_JSON);
            if (serverPrivateKey.getD() == null) {
                throw new IllegalStateException("Private key 'd' (private) parameter is missing.");
            }

            // Signer using server's private key
            JWSSigner signer = new ECDSASigner(serverPrivateKey);

            // Parse server's public key
            ECKey serverPublicKey = (ECKey) JWK.parse(SERVER_PUBLIC_KEY_JSON);

            // Compute SHA-256 hash of the server's public JWK to use as `kid`
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(serverPublicKey.toPublicJWK().toJSONString().getBytes(StandardCharsets.UTF_8));


            // Create the JWT header with the JWK object (the server public key)
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .type(JOSEObjectType.JWT)
                    .jwk(serverPublicKey.toPublicJWK())
                    .build();

            // Create JWT Payload
            long issuedAt = System.currentTimeMillis() / 1000; // Convert to seconds
            long paymentTime = System.currentTimeMillis(); // Use current time as payment time
            String transactionId = UUID.randomUUID().toString(); // Generate a unique transaction ID
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .claim("amount", amount) // Transaction amount
                    .claim("from", senderAccount) // Sender account
                    .claim("to", receiverAccount) // Receiver account
                    .claim("paymentMethod", "Bank deposit")
                    .claim("TranactionID", transactionId)
                    .claim("paymentTime", paymentTime)
                    .issueTime(new Date(issuedAt * 1000))
                    .expirationTime(new Date((issuedAt + (expirationTimeMs / 1000)) * 1000)) // Convert to milliseconds
                    .build();

            // Create JWT token
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);


            LOG.info("Transaction Cert Is: {}", signedJWT.serialize());
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new IllegalStateException("Error generating transaction certificate", e);
        }


    }}