package com.adorsys.webank.obs.serviceimpl;


import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;

import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;

import de.adorsys.webank.bank.api.service.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
    void testRegisterAccount_invalidJwt() {
        // Prepare test data
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setPublicKey("publicKey123");

        String phoneNumberCertificateJwt = "invalidJwt";

        // Mock JwtCertValidator's validateJWT method to return false
        when(jwtCertValidator.validateJWT(phoneNumberCertificateJwt)).thenReturn(false);

        // Call the method to test
        String result = obsService.registerAccount(registrationRequest, phoneNumberCertificateJwt);

        // Verify the result
        assertEquals("Invalid certificate or JWT. Account creation failed", result);

        // Verify the interaction with the mock
        verify(jwtCertValidator, times(1)).validateJWT(phoneNumberCertificateJwt);
        verify(bankAccountCertificateCreationService, times(0)).registerNewBankAccount(any(), any(), any(), anyString(), anyString());
    }


    @Test
    void testRegisterAccount_success() {
        // Prepare test data
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setPublicKey("publicKey123");

        String phoneNumberCertificateJwt = "validJwt";

        // Mock JwtCertValidator's validateJWT method to return true
        when(jwtCertValidator.validateJWT(phoneNumberCertificateJwt)).thenReturn(true);

        // Mock BankAccountCertificateCreationService's registerNewBankAccount method
        // Return a multi-line string so that lines[2] exists.
        String mockResult = "Header\nSubheader\nAccount ID: 12345";

        when(bankAccountCertificateCreationService.registerNewBankAccount(
                eq("1234567890"),  eq("publicKey123"),
                any(BankAccountBO.class),  anyString(),  anyString()
        )).thenReturn(mockResult);

        // Call the method to test
        String result = obsService.registerAccount(registrationRequest, phoneNumberCertificateJwt);

        // Verify the result
        assertEquals("Bank account successfully created. Details: " + mockResult, result);

        // Verify the interactions with the mocks
        verify(jwtCertValidator, times(1)).validateJWT(phoneNumberCertificateJwt);
        verify(bankAccountCertificateCreationService, times(1)).registerNewBankAccount(any(), any(), any(), anyString(), anyString());
    }

    @Test
    void testRegisterAccount_success_verifyBankAccountBO() {
        // Prepare test data
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setPublicKey("publicKey123");

        String phoneNumberCertificateJwt = "validJwt";

        // Mock JwtCertValidator's validateJWT method
        when(jwtCertValidator.validateJWT(phoneNumberCertificateJwt)).thenReturn(true);

        // Mock BankAccountCertificateCreationService
        String mockResult = "Header\nSubheader\nAccount ID: 12345";
        when(bankAccountCertificateCreationService.registerNewBankAccount(
                anyString(), anyString(), any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(mockResult);

        // Call the method
        obsService.registerAccount(registrationRequest, phoneNumberCertificateJwt);

        // Capture the BankAccountBO argument
        ArgumentCaptor<BankAccountBO> bankAccountCaptor = ArgumentCaptor.forClass(BankAccountBO.class);
        verify(bankAccountCertificateCreationService).registerNewBankAccount(
                eq("1234567890"), eq("publicKey123"), bankAccountCaptor.capture(), anyString(), anyString());

        // Assert BankAccountBO properties
        BankAccountBO capturedBankAccount = bankAccountCaptor.getValue();
        assertNotNull(capturedBankAccount);
        assertEquals("1234567890", capturedBankAccount.getMsisdn());
        assertEquals("XAF", capturedBankAccount.getCurrency().getCurrencyCode());
        assertEquals("Standard", capturedBankAccount.getProduct());
        assertEquals("72070032", capturedBankAccount.getBic());
        assertEquals("OBS", capturedBankAccount.getBranch());
    }


    @Test
    void testMakeTrans_success() {
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
    void testMakeTrans_accountNotFound() {
        String accountId = "nonExistent";

        // Mock BankAccountService to return null
        when(bankAccountService.getAccountById(accountId)).thenReturn(null);

        // Call the method
        String result = obsService.makeTrans(accountId);

        assertEquals("Bank account not found for ID: " + accountId, result);
    }

}
