package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.exception.ResourceNotFoundException;
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
     * @return A JSON string representing the transaction details or throws appropriate exceptions.
     */

    @Override
    public String getTrans(TransRequest transRequest) {
        try {
            log.info("Received transaction request: {}", transRequest);
            String accountId = transRequest.getAccountID();
            
            validateAccountExists(accountId);
            List<TransactionDetailsBO> postingLines = fetchTransactions(accountId);
            validateTransactionsExist(postingLines);
            
            List<String> transactionDetails = formatTransactionDetails(postingLines);
            log.info("Transaction details: {} " , transactionDetails);
            
            return formatJsonResponse(transactionDetails);
        } catch (Exception e) {
            handleTransactionError(e);
            throw e;
        }
    }

    private void validateAccountExists(String accountId) {
        BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
        if (bankAccount == null) {
            throw new AccountNotFoundException("Bank account not found for ID: " + accountId);
        }
    }

    private List<TransactionDetailsBO> fetchTransactions(String accountId) {
        LocalDateTime dateFrom = LocalDateTime.now().minusMonths(1);
        LocalDateTime dateTo = LocalDateTime.now();
        return bankAccountService.getTransactionsByDates(accountId, dateFrom, dateTo);
    }

    private void validateTransactionsExist(List<TransactionDetailsBO> postingLines) {
        if (postingLines.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for the given account and date range.");
        }
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

    private String formatJsonResponse(List<String> transactionDetails) {
        return "[\n" + String.join(",\n", transactionDetails) + "\n]";
    }

    private void handleTransactionError(Exception e) {
        if (e instanceof AccountNotFoundException || e instanceof ResourceNotFoundException) {
            return;
        }
        throw new ServiceUnavailableException("An error occurred while processing the request: " + e.getMessage());
    }
}