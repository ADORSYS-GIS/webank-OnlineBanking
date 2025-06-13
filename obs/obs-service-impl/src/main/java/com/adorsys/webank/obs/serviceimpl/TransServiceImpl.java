package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import lombok.RequiredArgsConstructor;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransServiceImpl implements TransServiceApi {


    private final BankAccountService bankAccountService;

    /**
     * Handles transaction requests by fetching transactions for a given account ID.
     *
     * @param transRequest The transaction request containing the account ID.
     * @return A JSON string representing the transaction details or an error message.
     */

    @Override
    public String getTrans(TransRequest transRequest) {
        try {
            log.info("Received transaction request: {}", transRequest);
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
                    .map(postingLine -> {
                        String amount = String.valueOf(postingLine.getTransactionAmount().getAmount());
                        String title = amount.startsWith("-") ? "Withdrawal" : "Deposit";
                        return "{\n" +
                                "  \"id\": \"" + postingLine.getTransactionId() + "\",\n" +
                                "  \"date\": \"" + postingLine.getBookingDate().toString() + "\",\n" +
                                "  \"amount\": \"" + amount + "\",\n" +
                                "  \"title\": \"" + title + "\"\n" +
                                "}";
                    })
                    .toList();

            log.info("Transaction details: {} " , transactionDetails);

            return "[\n" + String.join(",\n", transactionDetails) + "\n]";



        } catch (Exception e) {
            return "An error occurred while processing the request: " + e.getMessage();
        }
    }

}