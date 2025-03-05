package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.security.JwtCertValidator;
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
import de.adorsys.webank.bank.api.domain.TransactionDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class TransactionHelper {
    @Value("${server.private.key.json}")
    private String serverPrivateKeyJson;

    @Value("${server.public.key.json}")
    private String serverPublicKeyJson;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration-time-ms}")
    private Long expirationTimeMs;

    private static final String CURRENCY_CODE = "XAF";

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final JwtCertValidator jwtCertValidator;

    public TransactionHelper(BankAccountService bankAccountService,
                             TransactionService transactionService,
                             JwtCertValidator jwtCertValidator) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.jwtCertValidator = jwtCertValidator;
    }

    /**
     * Validates input and processes a transaction.
     * Returns error messages matching the test expectations.
     */
    public String validateAndProcessTransaction(String senderAccountId, String recipientAccountId,
                                                String amountStr, String accountCertJwt,
                                                Logger logger) {
        if (!isValidJwt(accountCertJwt, logger)) {
            return "Invalid certificate or JWT. Payout Request failed";
        }

        BigDecimal amount = parseAmount(amountStr, logger);
        if (amount == null) {
            return "Invalid amount format: " + amountStr;
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Amount must be a positive number";
        }

        BigDecimal balance = getCurrentBalance(senderAccountId, logger);
        if (balance == null) {
            return "Unable to retrieve balance for the source account";
        }
        if (balance.compareTo(amount) < 0) {
            return "Insufficient balance. Current balance: " + balance + " XAF";
        }

        return processTransaction(senderAccountId, recipientAccountId, amount, logger);
    }

    public boolean isValidJwt(String accountCertificateJwt, Logger logger) {
        try {
            boolean isValid = jwtCertValidator.validateJWT(accountCertificateJwt);
            logger.info("The AccountCert is: {}", accountCertificateJwt);
            return isValid;
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    public BigDecimal parseAmount(String amount, Logger logger) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            logger.error("Invalid amount format: {}", amount);
            return null;
        }
    }

    public BigDecimal getCurrentBalance(String accountId, Logger logger) {
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
            logger.error("Failed to retrieve account balance: {}", e.getMessage());
            return null;
        }
    }

    public String processTransaction(String senderAccountId, String recipientAccountId, BigDecimal amount, Logger logger) {
        BankAccountBO sendingAccount = bankAccountService.getAccountById(senderAccountId);
        BankAccountBO receivingAccount = bankAccountService.getAccountById(recipientAccountId);

        if (sendingAccount == null || receivingAccount == null) {
            return "One or both accounts not found";
        }

        MockBookingDetailsBO mockTransaction = createMockTransaction(
                sendingAccount.getIban(),
                receivingAccount.getIban(),
                amount
        );
        List<MockBookingDetailsBO> transactions = Collections.singletonList(mockTransaction);
        Map<String, String> errorMap = transactionService.bookMockTransaction(transactions);

        if (errorMap.isEmpty()) {
            logger.info("Transaction booked");
        } else {
            logger.error("Booking errors: {}", errorMap);
            return "Transaction failed due to booking errors";
        }

        String transactionCert = generateTransactionCert(senderAccountId, recipientAccountId, String.valueOf(amount));
        log.info("Transaction certificate: {}", transactionCert);
        return transactionCert + " Success";
    }

    private MockBookingDetailsBO createMockTransaction(String sendingIban, String receivingIban, BigDecimal amount) {
        MockBookingDetailsBO transaction = new MockBookingDetailsBO();
        transaction.setUserAccount(receivingIban);
        transaction.setOtherAccount(sendingIban);
        transaction.setAmount(amount);
        transaction.setCurrency(Currency.getInstance(CURRENCY_CODE));
        transaction.setBookingDate(LocalDate.now());
        transaction.setValueDate(LocalDate.now().plusDays(1));
        transaction.setCrDrName("Test User");
        transaction.setRemittance("Payment for testing purposes");
        return transaction;
    }

    public String generateTransactionCert(String senderId, String recipientId, String amount) {
        log.info("Starting certificate generation for transaction: senderId={}, recipientId={}, amount={}", senderId, recipientId, amount);
        try {
            // Parse the server's private key
            log.debug("Parsing server private key from JSON.");
            ECKey privateKey = (ECKey) JWK.parse(serverPrivateKeyJson);
            if (privateKey.getD() == null) {
                log.error("Private key parameter 'D' is missing in the server private key.");
                throw new IllegalStateException("Missing private key parameter");
            }
            log.debug("Server private key parsed successfully.");

            // Create signer using the private key
            JWSSigner signer = new ECDSASigner(privateKey);
            log.debug("JWSSigner created successfully using the private key.");

            // Parse the server's public key and build the JWT header
            log.debug("Parsing server public key from JSON.");
            ECKey publicKey = (ECKey) JWK.parse(serverPublicKeyJson);
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .type(JOSEObjectType.JWT)
                    .jwk(publicKey.toPublicJWK())
                    .build();
            log.debug("JWT header constructed successfully.");

            // Determine the period for transactions lookup and fetch transactions
            LocalDateTime dateFrom = LocalDateTime.now().minusMonths(1);
            log.info("Fetching transactions for senderId={} from {} to {}", senderId, dateFrom, LocalDateTime.now());
            List<TransactionDetailsBO> transactions = bankAccountService.getTransactionsByDates(senderId, dateFrom, LocalDateTime.now());
            log.debug("Retrieved {} transactions", transactions.size());

            // Determine the transaction ID from the most recent transaction
            String transactionId = transactions.stream()
                    .max(Comparator.comparing(TransactionDetailsBO::getBookingDate))
                    .map(TransactionDetailsBO::getTransactionId)
                    .orElse("1");
            log.info("Using transaction ID {} for certificate generation", transactionId);

            // Build the JWT claims
            long issuedAt = System.currentTimeMillis() / 1000;
            log.debug("Building JWT claims with issuedAt={}, expirationTimeMs={}", issuedAt, expirationTimeMs);
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .claim("amount", amount)
                    .claim("from", senderId)
                    .claim("to", recipientId)
                    .claim("paymentMethod", "Bank deposit")
                    .claim("TransactionID", transactionId)
                    .claim("paymentTime", System.currentTimeMillis())
                    .issueTime(new Date(issuedAt * 1000))
                    .expirationTime(new Date((issuedAt + expirationTimeMs / 1000) * 1000))
                    .build();
            log.debug("JWT claims built successfully.");

            // Create and sign the JWT
            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(signer);
            log.info("JWT signed successfully.");

            String serializedJWT = jwt.serialize();
            log.info("Certificate generation completed successfully.");
            return serializedJWT;
        } catch (Exception e) {
            log.error("Certificate generation failed: {}", e.getMessage(), e);
            throw new IllegalStateException("Certificate generation failed", e);
        }
    }

}
