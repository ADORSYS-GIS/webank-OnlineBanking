package com.adorsys.webank.obs.serviceimpl;


import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
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


}
