package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.InvalidJwtException;
import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import com.adorsys.webank.obs.service.WithdrawServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.adorsys.webank.config.SecurityUtils;
import java.time.LocalDateTime;
import com.adorsys.webank.exception.ServiceUnavailableException;

/**
 * Service implementation for handling withdrawal operations.
 * Processes withdrawal requests and manages the removal of funds from accounts.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawServiceApi {
    private final TransactionHelper transactionHelper;
    private final SignTransactionJwtValidator signTransactionValidator;

    /*     * The accountCertJwt is extracted from the JWT header and used to validate the transaction.
     * It is expected to be present in the JWT header under the key "accountJwt".
     */

    @Override
    public MoneyTransferResponse withdraw(MoneyTransferRequestDto request) {
        try {
            // Extract the JWT token from the current context
            Optional<String> jwtOpt = SecurityUtils.getCurrentUserJWT();
            if (jwtOpt.isEmpty()) {
                throw new IllegalStateException("No JWT token found in security context");
            }
            String jwtToken = jwtOpt.get();
            log.info("Processing withdrawal request for account: {}", request.getSenderAccountId());

            // Validate the transaction JWT
            if (!signTransactionValidator.validateSignTransactionJWT(jwtToken)) {
                throw new InvalidJwtException("Invalid transaction JWT");
            }

            // Process the transaction
            String result = transactionHelper.validateAndProcessTransaction(
                request.getSenderAccountId(),
                request.getRecipientAccountId(),
                request.getAmount().toPlainString(),
                jwtToken,
                log
            );

            // Create success response
            MoneyTransferResponse response = new MoneyTransferResponse();
            response.setStatus(MoneyTransferResponse.TransferStatus.COMPLETED);
            response.setTransactionId(result);
            response.setAmount(request.getAmount());
            response.setCurrency("XAF");
            response.setTimestamp(LocalDateTime.now());
            response.setMessage("Withdrawal completed successfully");

            log.info("Withdrawal processed successfully for account: {}", request.getSenderAccountId());
            return response;

        } catch (Exception e) {
            log.error("Error processing withdrawal request", e);
            throw new ServiceUnavailableException("An error occurred while processing the withdrawal: " + e.getMessage());
        }
    }
}
