package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.domain.MockBookingDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {
    private static final Logger LOG = LoggerFactory.getLogger(PayoutServiceImpl.class);
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
            return accountId + " Success";
        } else {
            LOG.error("Errors occurred while booking transaction(s): {}", errorMap);
            return "Transaction failed due to booking errors";
        }
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
}
