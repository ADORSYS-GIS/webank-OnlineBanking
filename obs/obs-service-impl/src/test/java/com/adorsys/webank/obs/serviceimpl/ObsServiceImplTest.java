package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.security.JwtCertValidator;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.BankAccountTransactionService;
import de.adorsys.webank.bank.api.service.util.BankAccountCertificateCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ObsServiceImplTest {

    @Mock
    private JwtCertValidator jwtCertValidator;

    @Mock
    private BankAccountCertificateCreationService bankAccountCertificateCreationService;

    @Mock
    private BankAccountTransactionService bankAccountTransactionService;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private ObsServiceImpl obsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerAccountWithInvalidJwt() {
        // Prepare test data
        String publicKey = "publicKey123";

        String phoneNumberCertificateJwt = "invalidJwt";

        // Mock JwtCertValidator's validateJWT method to return false
        when(jwtCertValidator.validateJWT(phoneNumberCertificateJwt)).thenReturn(false);

        // Call the method to test
        String result = obsService.registerAccount(publicKey, phoneNumberCertificateJwt);

        // Verify the result
        assertEquals("Invalid certificate or JWT. Account creation failed", result);

        // Verify the interaction with the mock
        verify(jwtCertValidator, times(1)).validateJWT(phoneNumberCertificateJwt);
        verify(bankAccountCertificateCreationService, times(0)).registerNewBankAccount(any(), any(), anyString(), anyString());
    }

    @Test
    void registerAccountSuccessfully() {
        // Prepare test data
        String publicKey = "publicKey123";

        String phoneNumberCertificateJwt = "validJwt";

        // Mock JwtCertValidator's validateJWT method to return true
        when(jwtCertValidator.validateJWT(phoneNumberCertificateJwt)).thenReturn(true);

        // Mock BankAccountCertificateCreationService's registerNewBankAccount method
        String mockResult = "Header\nSubheader\nAccount ID: 12345";
        when(bankAccountCertificateCreationService.registerNewBankAccount(eq("publicKey123"),
                any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(mockResult);

        // Call the method to test
        String result = obsService.registerAccount(publicKey, phoneNumberCertificateJwt);

        // Verify the result
        assertEquals("Bank account successfully created. Details: " + mockResult, result);

        // Verify the interactions with the mocks
        verify(jwtCertValidator, times(1)).validateJWT(phoneNumberCertificateJwt);
        verify(bankAccountCertificateCreationService, times(1)).registerNewBankAccount(any(), any(), anyString(), anyString());
    }

    @Test
    void registerAccountAndVerifyBankAccountProperties() {
        // Prepare test data
        String publicKey = "publicKey123";

        String phoneNumberCertificateJwt = "validJwt";

        // Mock JwtCertValidator's validateJWT method
        when(jwtCertValidator.validateJWT(phoneNumberCertificateJwt)).thenReturn(true);

        // Mock BankAccountCertificateCreationService
        String mockResult = "Header\nSubheader\nAccount ID: 12345";
        when(bankAccountCertificateCreationService.registerNewBankAccount(
         anyString(), any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(mockResult);

        // Call the method
        obsService.registerAccount(publicKey, phoneNumberCertificateJwt);

        // Capture the BankAccountBO argument
        ArgumentCaptor<BankAccountBO> bankAccountCaptor = ArgumentCaptor.forClass(BankAccountBO.class);
        verify(bankAccountCertificateCreationService).registerNewBankAccount(
             eq("publicKey123"), bankAccountCaptor.capture(), anyString(), anyString());

        // Assert BankAccountBO properties
        BankAccountBO capturedBankAccount = bankAccountCaptor.getValue();
        assertNotNull(capturedBankAccount);
        assertEquals("XAF", capturedBankAccount.getCurrency().getCurrencyCode());
        assertEquals("Standard", capturedBankAccount.getProduct());
        assertEquals("72070032", capturedBankAccount.getBic());
        assertEquals("OBS", capturedBankAccount.getBranch());
    }

    @Test
    void makeTransactionsSuccessfully() {
        String accountId = "12345";

        // Mock BankAccountService
        when(bankAccountService.getAccountById(accountId)).thenReturn(new BankAccountBO());

        // Call the method
        String result = obsService.makeTrans(accountId);

        // Verify deposits were made
        verify(bankAccountTransactionService, times(5))
                .depositCash(eq(accountId), any(), anyString());

        assertEquals("5 transactions completed successfully for account " + accountId, result);
    }

    @Test
    void makeTransactionsWithNonExistentAccount() {
        String accountId = "nonExistent";

        // Mock BankAccountService to return null
        when(bankAccountService.getAccountById(accountId)).thenReturn(null);

        // Call the method
        String result = obsService.makeTrans(accountId);

        assertEquals("Bank account not found for ID: " + accountId, result);
    }
}