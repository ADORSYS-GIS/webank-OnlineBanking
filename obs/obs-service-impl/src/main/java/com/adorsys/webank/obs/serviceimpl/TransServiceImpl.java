package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class TransServiceImpl implements TransServiceApi {

    private static final Logger log = LoggerFactory.getLogger(TransServiceImpl.class);
    private final BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;

    @Autowired
    public TransServiceImpl(BankAccountService bankAccountService, JwtCertValidator jwtCertValidator) {
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
                    .toList();

            log.info("Transaction details: " + transactionDetails.toString());

            return "[\n" + String.join(",\n", transactionDetails) + "\n]";



        } catch (Exception e) {
            return "An error occurred while processing the request: " + e.getMessage();
        }
    }

}