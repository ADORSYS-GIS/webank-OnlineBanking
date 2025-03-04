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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
            logger.info("Transaction booked:");
        } else {
            logger.error("Booking errors: {}", errorMap);
            return "Transaction failed due to booking errors";
        }

        String transactionCert = generateTransactionCert(senderAccountId, recipientAccountId, String.valueOf(amount));
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
        try {
            ECKey privateKey = (ECKey) JWK.parse(serverPrivateKeyJson);
            if (privateKey.getD() == null) {
                throw new IllegalStateException("Missing private key parameter");
            }

            JWSSigner signer = new ECDSASigner(privateKey);
            ECKey publicKey = (ECKey) JWK.parse(serverPublicKeyJson);

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .type(JOSEObjectType.JWT)
                    .jwk(publicKey.toPublicJWK())
                    .build();

            LocalDateTime dateFrom = LocalDateTime.now().minusMonths(1);
            List<TransactionDetailsBO> transactions = bankAccountService.getTransactionsByDates(senderId, dateFrom, LocalDateTime.now());

            String transactionId = transactions.stream()
                    .max(Comparator.comparing(TransactionDetailsBO::getBookingDate))
                    .map(TransactionDetailsBO::getTransactionId)
                    .orElse("1");

            long issuedAt = System.currentTimeMillis() / 1000;
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

            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(signer);

            return jwt.serialize();
        } catch (Exception e) {
            throw new IllegalStateException("Certificate generation failed", e);
        }
    }
}
