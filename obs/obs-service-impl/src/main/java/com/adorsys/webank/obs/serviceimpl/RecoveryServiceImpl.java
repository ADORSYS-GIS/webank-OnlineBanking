package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.RecoveryServiceApi;
import de.adorsys.webank.bank.api.service.util.BankAccountCertificateCreationService;
import org.springframework.stereotype.Service;

@Service
public class RecoveryServiceImpl implements RecoveryServiceApi {

    private final BankAccountCertificateCreationService bankAccountCertificateCreationService;

    public RecoveryServiceImpl(BankAccountCertificateCreationService bankAccountCertificateCreationService) {
        this.bankAccountCertificateCreationService = bankAccountCertificateCreationService;
    }

    @Override
    public String recoverAccount( String devPublicKey,String accountId) {
        return bankAccountCertificateCreationService.generateBankAccountCertificate(devPublicKey, accountId);
    }
}
