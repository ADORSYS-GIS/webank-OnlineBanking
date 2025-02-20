package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.config.RegistrationCache; // Add import for RegistrationCache
import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import de.adorsys.webank.bank.api.service.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Service
public class ObsServiceImpl implements RegistrationServiceApi {

    private static final Logger log = LoggerFactory.getLogger(ObsServiceImpl.class);

    private BankAccountCertificateCreationService bankAccountCertificateCreationService;
    private BankAccountService bankAccountService;
    private final JwtCertValidator jwtCertValidator;
    private final BankAccountTransactionService bankAccountTransactionService;

    // Add RegistrationCache as a dependency
    private final RegistrationCache registrationCache;

    public ObsServiceImpl(JwtCertValidator jwtCertValidator,
                          BankAccountTransactionService bankAccountTransactionService,
                          BankAccountService bankAccountService,
                          BankAccountCertificateCreationService bankAccountCertificateCreationService,
                          RegistrationCache registrationCache) {
        this.jwtCertValidator = jwtCertValidator;
        this.bankAccountTransactionService = bankAccountTransactionService;
        this.bankAccountService = bankAccountService;
        this.bankAccountCertificateCreationService = bankAccountCertificateCreationService;
        this.registrationCache = registrationCache;  // Initialize RegistrationCache
    }

    @Override
    public String registerAccount(RegistrationRequest registrationRequest, String phoneNumberCertificateJwt) {
        try {
            // Validate the JWT token passed from the frontend
            boolean isValid = jwtCertValidator.validateJWT(phoneNumberCertificateJwt);

            if (!isValid) {
                return "Invalid certificate or JWT. Account creation failed";
            }

            // Get phone number from registration request
            String phoneNumber = registrationRequest.getPhoneNumber();

            // Check if the phone number is already registered (present in the cache)
            if (registrationCache.isRegistered(phoneNumber)) {
                return "Phone number already registered.";
            }

            // Create and populate BankAccountBO
            String iban = UUID.randomUUID().toString();
            Currency currency = Currency.getInstance("XAF");
            String name = iban; // As name, we'll use the IBAN for now
            String product = "Standard";
            String bic = "72070032";
            String branch = "OBS";

            BankAccountBO bankAccountBO = BankAccountBO.builder()
                    .iban(iban)
                    .msisdn(phoneNumber)
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
            String createdAccountResult = bankAccountCertificateCreationService.registerNewBankAccount(
                    registrationRequest.getPhoneNumber(),
                    registrationRequest.getPublicKey(),
                    bankAccountBO,
                    UUID.randomUUID().toString(),
                    "OBS");

            // Split the string by newlines
            String[] lines = createdAccountResult.split("\n");

            // Access the account ID, which is in the third line (index 2)
            String accountId = lines[2];

            // Make the initial transaction (deposit)
            String deposit = makeTrans(accountId);
            log.info("Created account with id: {} and deposit amount: {}", accountId, deposit);

            // After account creation, add the phone number to the registration cache
            registrationCache.addToCache(phoneNumber);

            return "Bank account successfully created. Details: " + createdAccountResult;
        } catch (Exception e) {
            return "An error occurred while processing the request: " + e.getMessage();
        }
    }

    /**
     * Makes a transaction (in this case, a deposit) into a particular account.
     * <p>
     * The method:
     * 1. Validates the provided JWT.
     * 2. Retrieves the bank account using the account ID from the request.
     * 3. Extracts the deposit details (amount, currency, record user) from the request.
     * 4. Creates an AmountBO instance representing the deposit.
     * 5. Calls the depositCash method on the BankAccountService.
     */
    public String makeTrans(String accountId) {
        try {
            // Fetch the account details
            BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
            if (bankAccount == null) {
                return "Bank account not found for ID: " + accountId;
            }

            // Define multiple deposit values
            BigDecimal[] depositValues = {
                    new BigDecimal("1000.00"),
                    new BigDecimal("500.50"),
                    new BigDecimal("500.75"),
                    new BigDecimal("3000.00"),
                    new BigDecimal("1500.25")
            };

            Currency currency = Currency.getInstance("XAF");
            String recordUser = "Default name";

            // Process each transaction
            for (BigDecimal depositValue : depositValues) {
                AmountBO depositAmount = new AmountBO(currency, depositValue);
                bankAccountTransactionService.depositCash(accountId, depositAmount, recordUser);
            }

            return "5 transactions completed successfully for account " + accountId;

        } catch (Exception e) {
            return "An error occurred while processing the transactions: "
                    + (e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }
}
