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
import java.util.*;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;
    @Value("${server.private.key.json}")
    private String SERVER_PRIVATE_KEY_JSON;

    @Value("${server.public.key.json}")
    private String SERVER_PUBLIC_KEY_JSON;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration-time-ms}")
    private Long expirationTimeMs;

    private static final Logger log = LoggerFactory.getLogger(PayoutServiceImpl.class);

    public PayoutServiceImpl(TransactionService transactionService, BankAccountService bankAccountService, JwtCertValidator jwtCertValidator) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.jwtCertValidator = jwtCertValidator;
    }

    @Override
    public String payout(PayoutRequest payoutRequest, String accountCertificateJwt) {
        try {
            //validate the JWT token passed from the frontend
            boolean isValid = jwtCertValidator.validateJWT(accountCertificateJwt);
            log.info("The AccountCert is : {}", accountCertificateJwt);

            if (!isValid) {
                return "Invalid certificate or JWT. Payout Request failed";
            }
        }
        catch (Exception e){
             return "An error occurred while processing the request: " + e.getMessage();
            }


        // The particular account IBAN for which you want to mock transactions.
        String accountId = payoutRequest.getAccountID();
        String otherAccountID = payoutRequest.getOtherAccountID();
        String amount = payoutRequest.getAmount();
        log.info("amount from frontend is {}", payoutRequest.getAmount());
        log.info("Fetching IBANs for account IDs: {} and {}", accountId, otherAccountID);

        // Retrieve the bank accounts using the provided getAccountById method.
        BankAccountBO account1 = bankAccountService.getAccountById(accountId);
        BankAccountBO account2 = bankAccountService.getAccountById(otherAccountID);


        // Extract the IBANs from the retrieved accounts.
        String iban1 = account1.getIban();
        String iban2 = account2.getIban();

        // Log the retrieved IBANs.
        log.debug("Retrieved IBANs: {} and {}", iban1, iban2);
        // Create a mock transaction detail object.
        MockBookingDetailsBO mockTransaction = new MockBookingDetailsBO();

        // Set the account you want to test. For a payment transaction, typically this is the user account.
        mockTransaction.setUserAccount(iban1);
        // Set the counterparty account (beneficiary, merchant, etc.). This might be any valid IBAN.
        mockTransaction.setOtherAccount(iban2);

        // Set the transaction amount, currency, and other details.
        mockTransaction.setAmount(new BigDecimal(amount));
        mockTransaction.setCurrency(Currency.getInstance("XAF"));
        mockTransaction.setBookingDate(LocalDate.now());
        mockTransaction.setValueDate(LocalDate.now().plusDays(1));
        mockTransaction.setCrDrName("Test User");
        mockTransaction.setRemittance("Payment for testing purposes");



        // Add the mock transaction to a list (the service accepts a list of transactions).
        List<MockBookingDetailsBO> transactions = new ArrayList<>();
        transactions.add(mockTransaction);

        // Call the service to process the mock transactions.
        Map<String, String> errorMap = transactionService.bookMockTransaction(transactions);

        // Check for errors and output the result.
        if (errorMap.isEmpty()) {
            log.info("Mock transaction for account {} booked successfully.", accountId);
        } else {
            log.error("Errors occurred while booking transaction(s): {}", errorMap);
        }
        String transactionCert = generateTransactionCert(accountId, otherAccountID, amount);

        return transactionCert +  " Success";
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

            // Compute SHA-256 hash of the server’s public JWK to use as `kid`
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(serverPublicKey.toPublicJWK().toJSONString().getBytes(StandardCharsets.UTF_8));


            // Create the JWT header with the JWK object (the server public key)
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .type(JOSEObjectType.JWT)
                    .jwk(serverPublicKey.toPublicJWK())
                    .build();

            // Create JWT Payload
            long issuedAt = System.currentTimeMillis() / 1000; // Convert to seconds
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .claim("amount", amount) // Transaction amount
                    .claim("from", senderAccount) // Sender account
                    .claim("to", receiverAccount) // Receiver account
                    .issueTime(new Date(issuedAt * 1000))
                    .expirationTime(new Date((issuedAt + (expirationTimeMs / 1000)) * 1000)) // Convert to milliseconds
                    .build();

            // Create JWT token
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);


            log.info("Transaction Cert Is: {}", signedJWT.serialize());
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new IllegalStateException("Error generating transaction certificate", e);
        }
    }
    }
