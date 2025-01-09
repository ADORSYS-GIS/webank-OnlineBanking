package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.service.RegistrationServiceApi;

import de.adorsys.webank.bank.api.domain.AccountTypeBO;
import de.adorsys.webank.bank.api.domain.AccountUsageBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Service
public class ObsServiceImpl implements RegistrationServiceApi {

    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public String registerAccount(RegistrationRequest registrationRequest) {
        try {
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
            BankAccountBO createdAccount = bankAccountService.createNewAccount(bankAccountBO, UUID.randomUUID().toString(), "OBS");

            if (createdAccount != null) {
                return "Registration successful for phone number: " + registrationRequest.getPhoneNumber() +
                        ". Account ID: " + createdAccount.getId();
            } else {
                return "Error creating account for phone number: " + registrationRequest.getPhoneNumber();
            }
        } catch (Exception e) {
            return "An error occurred while processing the request: " + e.getMessage();
        }
    }
}

