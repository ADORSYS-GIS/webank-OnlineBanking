package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.WithdrawRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import com.adorsys.webank.obs.service.WithdrawServiceApi;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WithdrawServiceImpl implements WithdrawServiceApi {
    private static final Logger LOG = LoggerFactory.getLogger(WithdrawServiceImpl.class);

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
    private final SignTransactionJwtValidator signTransactionJwtValidator;

    public WithdrawServiceImpl(TransactionService transactionService, BankAccountService bankAccountService, JwtCertValidator jwtCertValidator, SignTransactionJwtValidator signTransactionJwtValidator) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.jwtCertValidator = jwtCertValidator;
        this.signTransactionJwtValidator = signTransactionJwtValidator;
    }



    @Override
    public String withdraw(WithdrawRequest withdrawRequest, String accountCertificateJwt) {
        // Validate the basic JWT first
        if (!isValidJwt(accountCertificateJwt)) {
            return "Invalid certificate or JWT. Withdrawal Request failed";
        }


// Now, validate the transaction JWT using SignTransactionJwtValidator
        SignTransactionJwtValidator signTransactionJwtValidator = new SignTransactionJwtValidator();
        boolean isValidTransaction = signTransactionJwtValidator.validateSignTransactionJWT(accountCertificateJwt);
        if (!isValidTransaction) {
            return "Invalid transaction JWT. Withdrawal Request failed";
        }

// Proceed with the withdrawal logic if all validations pass...


        BigDecimal amountToSend = parseAmount(withdrawRequest.getAmount());
        if (amountToSend == null) {
            return "Invalid amount format: " + withdrawRequest.getAmount();
        }
        if (amountToSend.compareTo(BigDecimal.ZERO) <= 0) {
            return "Amount must be a positive number";
        }

        // Retrieve account IDs from the DTO using the given names.
        String senderAccountId = withdrawRequest.getSenderAccountId();

        // Check the balance for the sender account.
        BigDecimal currentBalance = getCurrentBalance(senderAccountId);
        if (currentBalance == null) {
            return "Unable to retrieve balance for the source account";
        }
        if (currentBalance.compareTo(amountToSend) < 0) {
            return "Insufficient balance. Current balance: " + currentBalance + " XAF";
        }

        String recipientAccountId = withdrawRequest.getRecipientAccountId();

        // Call processTransaction with senderAccountId first and recipientAccountId second.
        return processTransaction(senderAccountId, recipientAccountId, amountToSend);
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

    private String processTransaction(String senderAccountId, String recipientAccountId, BigDecimal amount) {
        BankAccountBO sendingAccount = bankAccountService.getAccountById(senderAccountId);
        BankAccountBO receivingAccount = bankAccountService.getAccountById(recipientAccountId);

        if (sendingAccount == null || receivingAccount == null) {
            return "One or both accounts not found";
        }

        // Create the mock transaction using the proper IBANs.
        MockBookingDetailsBO mockTransaction = createMockTransaction(sendingAccount.getIban(), receivingAccount.getIban(), amount);
        List<MockBookingDetailsBO> transactions = Collections.singletonList(mockTransaction);
        Map<String, String> errorMap = transactionService.bookMockTransaction(transactions);

        if (errorMap.isEmpty()) {
            LOG.info("Mock transaction for sender account {} and recipient account {} booked successfully.", senderAccountId, recipientAccountId);
        } else {
            LOG.error("Errors occurred while booking transaction(s): {}", errorMap);
            return "Transaction failed due to booking errors";
        }

        // Generate the transaction certificate with the correct order.
        String transactionCert = generateTransactionCert(senderAccountId, recipientAccountId, String.valueOf(amount));
        return transactionCert + " Success";
    }

    private MockBookingDetailsBO createMockTransaction(String sendingAccountIban, String receivingAccountIban, BigDecimal amount) {
        MockBookingDetailsBO mockTransaction = new MockBookingDetailsBO();
        // Note: Adjust the assignment below if you intend a different order for the IBANs.
        mockTransaction.setUserAccount(receivingAccountIban);
        mockTransaction.setOtherAccount(sendingAccountIban);
        mockTransaction.setAmount(amount);
        mockTransaction.setCurrency(Currency.getInstance(CURRENCY_CODE));
        mockTransaction.setBookingDate(LocalDate.now());
        mockTransaction.setValueDate(LocalDate.now().plusDays(1));
        mockTransaction.setCrDrName("Test User");
        mockTransaction.setRemittance("Payment for testing purposes");
        return mockTransaction;
    }

    public String generateTransactionCert(String senderAccountId, String recipientAccountId, String amount) {
        try {
            // Retrieve the latest transaction for senderAccountId within the last month.
            LocalDateTime dateFrom = LocalDateTime.now().minusMonths(1);
            LocalDateTime dateTo = LocalDateTime.now();
            List<TransactionDetailsBO> transactions = bankAccountService.getTransactionsByDates(senderAccountId, dateFrom, dateTo);
            Optional<TransactionDetailsBO> latestTransactionOpt = transactions.stream()
                    .max(Comparator.comparing(TransactionDetailsBO::getBookingDate));
            String transactionId;
            if (latestTransactionOpt.isPresent()) {
                transactionId = latestTransactionOpt.get().getTransactionId();
            } else {
                transactionId = "1";
            }

            // Parse server's private key from JWK JSON.
            ECKey serverPrivateKey = (ECKey) JWK.parse(SERVER_PRIVATE_KEY_JSON);
            if (serverPrivateKey.getD() == null) {
                throw new IllegalStateException("Private key 'd' (private) parameter is missing.");
            }

            // Create a signer using the server's private key.
            JWSSigner signer = new ECDSASigner(serverPrivateKey);

            // Parse server's public key.
            ECKey serverPublicKey = (ECKey) JWK.parse(SERVER_PUBLIC_KEY_JSON);

            // Create the JWT header with the server public key.
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .type(JOSEObjectType.JWT)
                    .jwk(serverPublicKey.toPublicJWK())
                    .build();

            // Create the JWT payload.
            long issuedAt = System.currentTimeMillis() / 1000; // seconds
            long paymentTime = System.currentTimeMillis();
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .claim("amount", amount)              // Transaction amount.
                    .claim("from", senderAccountId)         // Sender account.
                    .claim("to", recipientAccountId)        // Recipient account.
                    .claim("paymentMethod", "Bank deposit")
                    .claim("TranactionID", transactionId)
                    .claim("paymentTime", paymentTime)
                    .issueTime(new Date(issuedAt * 1000))
                    .expirationTime(new Date((issuedAt + (expirationTimeMs / 1000)) * 1000))
                    .build();

            // Create and sign the JWT.
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);

            LOG.info("Transaction Cert Is: {}", signedJWT.serialize());
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new IllegalStateException("Error generating transaction certificate", e);
        }
    }
}
