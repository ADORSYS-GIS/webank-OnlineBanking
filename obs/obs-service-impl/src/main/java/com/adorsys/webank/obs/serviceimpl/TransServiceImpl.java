package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.TransServiceApi;
import de.adorsys.ledgers.postings.api.service.LedgerService;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.TransactionDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.BankAccountTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransServiceImpl implements TransServiceApi {

    private static final Logger log = LoggerFactory.getLogger(TransServiceImpl.class);
    private final BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;

    @Autowired
    public TransServiceImpl(BankAccountService bankAccountService, JwtCertValidator jwtCertValidator, BankAccountTransactionService bankAccountTransactionService, LedgerService ledgerService) {
        this.bankAccountService = bankAccountService;
        this.jwtCertValidator = jwtCertValidator;
    }



    @Override
    public String getTrans(TransRequest transRequest, String accountCertificateJwt) {
        try {
            // Validate the JWT certificate
            boolean isValid = jwtCertValidator.validateJWT(accountCertificateJwt);
            if (!isValid) {
                return "Invalid certificate or JWT. Transaction retrieval failed.";
            }

            // Extract the account ID from the request
            String accountId = transRequest.getAccountID();

            // Fetch the account details
            BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
            if (bankAccount == null) {
                return "Bank account not found for ID: " + accountId;
            }

            // Define the date range for transactions (default to last month)
            LocalDateTime dateFrom = LocalDateTime.now().minusMonths(1);
            LocalDateTime dateTo = LocalDateTime.now();


            // Fetch the transactions using the ledger service
            List<TransactionDetailsBO> postingLines = bankAccountService.getTransactionsByDates(accountId, dateFrom, dateTo);

            // If no transactions found
            if (postingLines.isEmpty()) {
                return "No transactions found for the given account and date range.";
            }

            // Map the posting lines to a properly formatted JSON string
            List<String> transactionDetails = postingLines.stream()
                    .map(postingLine -> "{\n" +
                            "  \"id\": \"" + postingLine.getTransactionId() + "\",\n" +
                            "  \"date\": \"" + postingLine.getBookingDate().toString() + "\",\n" +
                            "  \"amount\": \"" + postingLine.getTransactionAmount().getAmount() + "\",\n" +
                            "  \"title\": \"" + "Deposit" + "\"\n" +
                            "}")
                    .collect(Collectors.toList());

            log.info("Transaction details: " + transactionDetails.toString());

            return "[\n" + String.join(",\n", transactionDetails) + "\n]";



        } catch (Exception e) {
            return "An error occurred while processing the request: " + e.getMessage();
        }
    }

}