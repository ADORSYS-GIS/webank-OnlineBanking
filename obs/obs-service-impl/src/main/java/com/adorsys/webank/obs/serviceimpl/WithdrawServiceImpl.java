package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import com.adorsys.webank.obs.service.WithdrawServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.adorsys.webank.config.SecurityUtils;

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
    public String withdraw(MoneyTransferRequestDto request) {

        // extract the JWT token from the current context to be used to verify transaction jwt
        Optional<String> jwtOpt = SecurityUtils.getCurrentUserJWT();
        if (jwtOpt.isEmpty()) {
            throw new IllegalStateException("No JWT token found in security context");
        }
        String jwtToken = jwtOpt.get();

        log.info("jwt token from current sprint context is {}", jwtToken);

        if (!signTransactionValidator.validateSignTransactionJWT(jwtToken)) {
            return "Invalid transaction JWT";
        }
        // Delegate common validation and processing to TransactionHelper
        return transactionHelper.validateAndProcessTransaction(
                request.getSenderAccountId(),
                request.getRecipientAccountId(),
                request.getAmount(),
                jwtToken,
                log
        );
    }
}
