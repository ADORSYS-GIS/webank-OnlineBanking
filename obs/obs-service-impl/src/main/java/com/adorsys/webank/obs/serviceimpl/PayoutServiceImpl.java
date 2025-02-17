package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.SignedJWT;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.MockBookingDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;

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
        String OtherAccount = payoutRequest.getOtherAccountID();
        String amount = payoutRequest.getAmount();
        log.info("amount from frontend is {}", payoutRequest.getAmount());
        log.info("Fetching IBANs for account IDs: {} and {}", accountId, OtherAccount);

        // Retrieve the bank accounts using the provided getAccountById method.
        BankAccountBO account1 = bankAccountService.getAccountById(accountId);
        BankAccountBO account2 = bankAccountService.getAccountById(OtherAccount);


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
        return accountId + "Success";
    }
    }
