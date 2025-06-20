package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.RecoveryServiceApi;
import de.adorsys.webank.bank.api.service.util.BankAccountCertificateCreationService;
import org.springframework.stereotype.Service;
import com.adorsys.webank.config.SecurityUtils;
import com.nimbusds.jose.jwk.ECKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecoveryServiceImpl implements RecoveryServiceApi {

    private final BankAccountCertificateCreationService bankAccountCertificateCreationService;

    /**
     * This method is used to recover an account by generating a bank account certificate.
     * It extracts the device public key from the security context and uses it to create the certificate.
     *
     * @param accountId The ID of the account to be recovered.
     * @return A string representing the bank account certificate.
     */

    @Override
    public String recoverAccount( String accountId) {

        ECKey devicePub = SecurityUtils.extractDeviceJwkFromContext();

        return bankAccountCertificateCreationService.generateBankAccountCertificate(String.valueOf(devicePub), accountId);
    }
}
