package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import de.adorsys.webank.bank.api.domain.AccountTypeBO;
import de.adorsys.webank.bank.api.domain.AccountUsageBO;
import de.adorsys.webank.bank.api.domain.AmountBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.BankAccountTransactionService;
import de.adorsys.webank.bank.api.service.util.BankAccountCertificateCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObsServiceImpl implements RegistrationServiceApi {

    private final BankAccountCertificateCreationService bankAccountCertificateCreationService;
    private final BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;
    private final BankAccountTransactionService bankAccountTransactionService;

    @Override
    @Transactional
    public String registerAccount(String publicKey, String registrationJwt) {
        if (log.isInfoEnabled()) {
            log.info("Registering account with publicKey: {} and registrationJwt: {}", publicKey, registrationJwt);
        }
        try {
            boolean isValid = jwtCertValidator.validateJWT(registrationJwt);

            if (!isValid) {
                return "Invalid certificate or JWT. Account creation failed";
            }

            // Iban will come from configuration
            String iban = UUID.randomUUID().toString();
            String msidn =  UUID.randomUUID().toString();
            Currency currency = Currency.getInstance("XAF");
            String name = iban;
            String product = "Standard";
            String bic = "72070032";
            String branch = "OBS";

            // Create and populate BankAccountBO with balance set
            BankAccountBO bankAccountBO = BankAccountBO.builder()
                    .iban(iban)
                    .msisdn(msidn)
                    .currency(currency)
                    .name(name)
                    .displayName(name)
                    .product(product)
                    .accountType(AccountTypeBO.CACC)
                    .bic(bic)
                    .usageType(AccountUsageBO.PRIV)
                    .blocked(false)
                    .systemBlocked(false)
                    .creditLimit(BigDecimal.ZERO)
                    .branch(branch)
                    .build();

            // Call the service to create the account
            String createdAccountResult = bankAccountCertificateCreationService.registerNewBankAccount(publicKey, bankAccountBO, UUID.randomUUID().toString(), "OBS");

            // Split the string by newlines
            String[] lines = createdAccountResult.split("\n");

            // Access the account ID, which is in the third line (index 2)
            String accountId = lines[2];

            // Make the deposit transaction
            String deposit = makeTrans(accountId);
            if (log.isInfoEnabled()) {
                log.info("Created account with id: {} and deposit amount: {}", accountId, deposit);
            }

            return "Bank account successfully created. Details: " + createdAccountResult;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("An error occurred while processing the request: {}", e.getMessage(), e);
            }
            return "An error occurred while processing the request: " + e.getMessage();
        }
    }

    @Transactional
    public String makeTrans(String accountId) {
        if (log.isInfoEnabled()) {
            log.info("Processing transaction for accountId: {}", accountId);
        }
        try {
            // Fetch the account details
            BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
            if (bankAccount == null) {
                if (log.isErrorEnabled()) {
                    log.error("Bank account not found for accountId: {}", accountId);
                }
                return "Bank account not found for ID: " + accountId;
            }

            // Define multiple deposit values
            BigDecimal[] depositValues = {
            };

            Currency currency = Currency.getInstance("XAF");
            String recordUser = "Default name";

            // Process each transaction
            for (BigDecimal depositValue : depositValues) {
                AmountBO depositAmount = new AmountBO(currency, depositValue);
                if (log.isInfoEnabled()) {
                    log.info("Processing deposit of {} for accountId: {}", depositValue, accountId);
                }
                bankAccountTransactionService.depositCash(accountId, depositAmount, recordUser);
            }

            return "5 transactions completed successfully for account " + accountId;

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("An error occurred while processing the transactions for accountId: {}: {}", accountId, e.getMessage(), e);
            }
            return "An error occurred while processing the transactions: "
                    + (e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }
}
