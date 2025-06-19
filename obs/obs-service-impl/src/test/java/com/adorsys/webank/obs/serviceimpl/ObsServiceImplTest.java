package com.adorsys.webank.obs.serviceimpl;

import de.adorsys.webank.bank.api.domain.AccountTypeBO;
import de.adorsys.webank.bank.api.domain.AccountUsageBO;
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
import org.mockito.MockedStatic;
import java.math.BigDecimal;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.adorsys.webank.config.SecurityUtils;
import com.adorsys.webank.obs.dto.response.RegistrationResponse;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import org.junit.jupiter.api.AfterEach;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.JOSEException;

class ObsServiceImplTest {

    @Mock
    private BankAccountCertificateCreationService bankAccountCertificateCreationService;

    @Mock
    private BankAccountTransactionService bankAccountTransactionService;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private ObsServiceImpl obsService;

    private MockedStatic<SecurityUtils> securityUtilsMock;

    @BeforeEach
    void setUp() throws JOSEException {
        MockitoAnnotations.openMocks(this);
        
        // Create a test ECKey
        ECKey testECKey = new ECKeyGenerator(Curve.P_256)
                .keyID("test-key-id")
                .generate();

        // Mock SecurityUtils to return our test ECKey
        securityUtilsMock = mockStatic(SecurityUtils.class);
        securityUtilsMock.when(SecurityUtils::extractDeviceJwkFromContext)
            .thenReturn(testECKey);
    }

    @AfterEach
    void tearDown() {
        if (securityUtilsMock != null) {
            securityUtilsMock.close();
        }
    }

    @Test
    void registerAccountWithInvalidJwt() {
        // Prepare test data
        String publicKey = "publicKey123";

        // Mock BankAccountCertificateCreationService to return null for invalid cases
        when(bankAccountCertificateCreationService.registerNewBankAccount(
                anyString(), any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(null);

        // Call the method to test
        RegistrationResponse result = obsService.registerAccount(publicKey);

        // Verify the result
        assertEquals(RegistrationResponse.RegistrationStatus.FAILED, result.getStatus());
        assertTrue(result.getMessage().contains("An error occurred while processing the request"));

        verify(bankAccountCertificateCreationService, times(1)).registerNewBankAccount(anyString(), any(BankAccountBO.class), anyString(), anyString());
    }

    @Test
    void registerAccountSuccessfully() {
        // Prepare test data
        String publicKey = "publicKey123";

        // Mock BankAccountCertificateCreationService's registerNewBankAccount method
        String mockResult = "Header\nSubheader\nAccount ID: 12345";
        when(bankAccountCertificateCreationService.registerNewBankAccount(
                anyString(), any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(mockResult);

        // Call the method to test
        RegistrationResponse result = obsService.registerAccount(publicKey);

        // Verify the result
        assertEquals(RegistrationResponse.RegistrationStatus.SUCCESS, result.getStatus());
        assertEquals("Bank account successfully created. Details: " + mockResult, result.getMessage());

        verify(bankAccountCertificateCreationService, times(1)).registerNewBankAccount(anyString(), any(BankAccountBO.class), anyString(), anyString());
    }

    @Test
    void registerAccountAndVerifyBankAccountProperties() {
        // Prepare test data
        String publicKey = "publicKey123";

        // Mock BankAccountCertificateCreationService
        String mockResult = "Header\nSubheader\nAccount ID: 12345";
        when(bankAccountCertificateCreationService.registerNewBankAccount(
                anyString(), any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(mockResult);

        // Call the method
        RegistrationResponse result = obsService.registerAccount(publicKey);

        // Capture the arguments
        ArgumentCaptor<String> ecKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BankAccountBO> bankAccountCaptor = ArgumentCaptor.forClass(BankAccountBO.class);
        ArgumentCaptor<String> randomUuidCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> branchCaptor = ArgumentCaptor.forClass(String.class);

        // Verify the method was called with the correct arguments
        verify(bankAccountCertificateCreationService).registerNewBankAccount(
            ecKeyCaptor.capture(),
            bankAccountCaptor.capture(),
            randomUuidCaptor.capture(),
            branchCaptor.capture()
        );

        // Verify the ECKey string starts with the expected JSON structure
        assertTrue(ecKeyCaptor.getValue().startsWith("{\"kty\":\"EC\""));

        // Verify the BankAccountBO properties
        BankAccountBO capturedAccount = bankAccountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertNotNull(capturedAccount.getIban());
        assertNotNull(capturedAccount.getMsisdn());
        assertEquals("XAF", capturedAccount.getCurrency().getCurrencyCode());
        assertEquals("Standard", capturedAccount.getProduct());
        assertEquals("72070032", capturedAccount.getBic());
        assertEquals(AccountTypeBO.CACC, capturedAccount.getAccountType());
        assertEquals(AccountUsageBO.PRIV, capturedAccount.getUsageType());
        assertFalse(capturedAccount.isBlocked());
        assertFalse(capturedAccount.isSystemBlocked());
        assertEquals(BigDecimal.ZERO, capturedAccount.getCreditLimit());
        assertEquals("OBS", capturedAccount.getBranch());

        // Verify random UUID was generated
        assertTrue(randomUuidCaptor.getValue().matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));

        // Verify branch is correct
        assertEquals("OBS", branchCaptor.getValue());

        // Verify response
        assertEquals(RegistrationResponse.RegistrationStatus.SUCCESS, result.getStatus());
        assertEquals("Bank account successfully created. Details: " + mockResult, result.getMessage());
    }

    @Test
    void makeTransactionsSuccessfully() {
        String accountId = "12345";
        BankAccountBO mockAccount = new BankAccountBO();
        mockAccount.setIban("TEST123");
        mockAccount.setCurrency(Currency.getInstance("XAF"));

        // Mock BankAccountService
        when(bankAccountService.getAccountById(accountId)).thenReturn(mockAccount);

        // Call the method
        String result = obsService.makeTrans(accountId);

        // Verify the result message
        assertEquals("5 transactions completed successfully for account " + accountId, result);
    }

    @Test
    void makeTransactionsWithNonExistentAccount() {
        String accountId = "nonExistent";

        // Mock BankAccountService to return null
        when(bankAccountService.getAccountById(accountId)).thenReturn(null);

        // Call the method
        String result = obsService.makeTrans(accountId);

        // Verify no transactions were attempted
        verify(bankAccountTransactionService, never()).depositCash(anyString(), any(), anyString());
        assertEquals("Bank account not found for ID: " + accountId, result);
    }

    @Test
    void registerAccountWhenDeviceKeyIsMissing() {
        securityUtilsMock.close();

        securityUtilsMock = mockStatic(SecurityUtils.class);
        securityUtilsMock.when(SecurityUtils::extractDeviceJwkFromContext).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class , () ->
            obsService.registerAccount("jwt-token")
        );

        assertEquals("Device public key not found in security context. Please ensure the user is authenticated.", exception.getMessage());


    }
}