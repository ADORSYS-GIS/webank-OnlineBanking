package com.adorsys.webank.obs.serviceimpl;


import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.security.JwtCertValidator;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.util.BankAccountCertificateCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import de.adorsys.webank.bank.api.service.BankAccountTransactionService;
import de.adorsys.webank.bank.api.service.BankAccountService;


public class ObsServiceImplTest {

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

        Mockito.when(bankAccountCertificateCreationService.registerNewBankAccount(
                Mockito.eq("1234567890"),  Mockito.eq("publicKey123"),
                Mockito.any(BankAccountBO.class),  Mockito.anyString(),  Mockito.anyString()
        )).thenReturn(mockResult);

        // Call the method to test
        String result = obsService.registerAccount(registrationRequest, phoneNumberCertificateJwt);

        // Verify the result
        assertEquals("Bank account successfully created. Details: " + mockResult, result);

        // Verify the interactions with the mocks
        verify(jwtCertValidator, times(1)).validateJWT(phoneNumberCertificateJwt);
        verify(bankAccountCertificateCreationService, times(1)).registerNewBankAccount(any(), any(), any(), anyString(), anyString());
    }


}
