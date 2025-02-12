package com.adorsys.webank.obs.serviceimpl;

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


    public ObsServiceImpl(JwtCertValidator jwtCertValidator, BankAccountTransactionService bankAccountTransactionService, BankAccountService bankAccountService, BankAccountCertificateCreationService bankAccountCertificateCreationService) {
        this.jwtCertValidator = jwtCertValidator;
        this.bankAccountTransactionService = bankAccountTransactionService;
        this.bankAccountService = bankAccountService;
        this.bankAccountCertificateCreationService = bankAccountCertificateCreationService;
    }


    @Override
    public String registerAccount(RegistrationRequest registrationRequest, String phoneNumberCertificateJwt ) {

        try {

            //validate the JWT token passed from the frontend
            boolean isValid = jwtCertValidator.validateJWT(phoneNumberCertificateJwt);

            if (!isValid){
                return "Invalid certificate or JWT. Account creation failed";
            }
            // Iban will come from configuration
            String iban = UUID.randomUUID().toString();
            String msidn = registrationRequest.getPhoneNumber();
            // currency will come from config
            Currency currency = Currency.getInstance("XAF");
            // As name we will use the public key id for now. FixMe
            String name = iban;
            // product will come from config
            String product = "Standard";
            // Bic will come from ASPSP config
            String bic = "72070032";
            // Branch will come from config
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
            String createdAccountResult = bankAccountCertificateCreationService.registerNewBankAccount(registrationRequest.getPhoneNumber(), registrationRequest.getPublicKey(), bankAccountBO, UUID.randomUUID().toString(), "OBS");

            // Split the string by newlines
            String[] lines = createdAccountResult.split("\n");

            // Access the account ID, which is in the third line (index 2)
            String accountId = lines[2];

            String deposit = makeTrans(accountId);
            log.info("Created account with id: {} and deposit amount: {}", accountId, deposit);

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

            // Fetch the account details.
            BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
            if (bankAccount == null) {
                return "Bank account not found for ID: " + accountId;
            }

            // Define multiple deposit values.
            BigDecimal[] depositValues = {
                    new BigDecimal("1000.00"),
                    new BigDecimal("500.50"),
                    new BigDecimal("500.75"),
                    new BigDecimal("3000.00"),
                    new BigDecimal("1500.25")
            };

            Currency currency = Currency.getInstance("XAF");
            String recordUser = "Default name";

            // Process each transaction.
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

