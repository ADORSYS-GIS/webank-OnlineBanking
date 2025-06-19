package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.exception.ResourceNotFoundException;
import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.dto.response.TransactionHistoryResponse;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
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
     * @return A JSON string representing the transaction details or throws appropriate exceptions.
     */

    @Override
    public TransactionHistoryResponse getTrans(TransRequest transRequest) {
        try {
            log.info("Received transaction request: {}", transRequest);
            
            // Extract the account ID from the request
            String accountId = transRequest.getAccountID();

            // Fetch the account details
            BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
            if (bankAccount == null) {
                TransactionHistoryResponse response = new TransactionHistoryResponse();
                response.setStatus(TransactionHistoryResponse.TransactionStatus.FAILED);
                response.setMessage("Bank account not found for ID: " + accountId);
                response.setTimestamp(LocalDateTime.now());
                response.setData("[]");
                return response;
            }

            // Define the date range for transactions (default to last month)
            LocalDateTime dateFrom = LocalDateTime.now().minusMonths(1);
            LocalDateTime dateTo = LocalDateTime.now();

            // Fetch the transactions using the ledger service
            List<TransactionDetailsBO> postingLines = bankAccountService.getTransactionsByDates(accountId, dateFrom, dateTo);

            // Create the response
            TransactionHistoryResponse response = new TransactionHistoryResponse();
            response.setStatus(TransactionHistoryResponse.TransactionStatus.SUCCESS);
            response.setMessage("Transaction history retrieved successfully");
            response.setTimestamp(LocalDateTime.now());

            // If no transactions found
            if (postingLines.isEmpty()) {
                response.setData("[]");
                return response;
            }

    private List<String> formatTransactionDetails(List<TransactionDetailsBO> postingLines) {
        return postingLines.stream()
                .map(this::formatTransactionDetail)
                .toList();
    }

    private String formatTransactionDetail(TransactionDetailsBO postingLine) {
        String amount = String.valueOf(postingLine.getTransactionAmount().getAmount());
        String title = amount.startsWith("-") ? "Withdrawal" : "Deposit";
        return "{\n" +
                "  \"id\": \"" + postingLine.getTransactionId() + "\",\n" +
                "  \"date\": \"" + postingLine.getBookingDate().toString() + "\",\n" +
                "  \"amount\": \"" + amount + "\",\n" +
                "  \"title\": \"" + title + "\"\n" +
                "}";
    }

            String transactionsJson = "[\n" + String.join(",\n", transactionDetails) + "\n]";
            response.setData(transactionsJson);
            
            log.info("Successfully processed transaction history for account {}", accountId);
            return response;

        } catch (Exception e) {
            log.error("Error processing transaction request: {}", e.getMessage(), e);
            TransactionHistoryResponse response = new TransactionHistoryResponse();
            response.setStatus(TransactionHistoryResponse.TransactionStatus.FAILED);
            response.setMessage("An error occurred while processing the request: " + e.getMessage());
            response.setTimestamp(LocalDateTime.now());
            response.setData("[]");
            return response;
        }
    }
}