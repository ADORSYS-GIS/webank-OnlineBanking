package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import de.adorsys.webank.bank.api.service.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Field;


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

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private ObsServiceImpl obsService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Manually inject RedisTemplate mock using reflection
        Field redisTemplateField = ObsServiceImpl.class.getDeclaredField("redisTemplate");
        redisTemplateField.setAccessible(true);
        redisTemplateField.set(obsService, redisTemplate);
    }

    @Test
    void testRegisterAccount_invalidJwt() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setPublicKey("publicKey123");
        String jwt = "invalidJwt";

        when(redisTemplate.hasKey("1234567890")).thenReturn(false);
        when(jwtCertValidator.validateJWT(jwt)).thenReturn(false);

        String result = obsService.registerAccount(registrationRequest, jwt);

        assertEquals("Invalid certificate or JWT. Account creation failed", result);
        verify(redisTemplate).hasKey("1234567890");
        verify(jwtCertValidator).validateJWT(jwt);
        verifyNoInteractions(bankAccountCertificateCreationService);
    }

    @Test
    void testRegisterAccount_phoneAlreadyRegistered() {
        RegistrationRequest request = new RegistrationRequest();
        request.setPhoneNumber("1234567890");
        String jwt = "anyJwt";

        when(redisTemplate.hasKey("1234567890")).thenReturn(true);

        String result = obsService.registerAccount(request, jwt);

        assertEquals("Phone number is already registered.", result);
        verify(redisTemplate).hasKey("1234567890");
        verifyNoInteractions(jwtCertValidator, bankAccountCertificateCreationService);
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

        // Mock RedisTemplate's opsForValue() to return a valid mock
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // Mock BankAccountCertificateCreationService's registerNewBankAccount method
        String mockResult = "Header\nSubheader\nAccount ID: 12345";
        when(bankAccountCertificateCreationService.registerNewBankAccount(
                eq("1234567890"), eq("publicKey123"),
                any(BankAccountBO.class), anyString(), anyString()
        )).thenReturn(mockResult);

        // Call the method to test
        String result = obsService.registerAccount(registrationRequest, phoneNumberCertificateJwt);

        // Adjust the expected message to match the multi-line actual result
        String expectedMessage = "Bank account successfully created. Details: Header\nSubheader\nAccount ID: 12345";

        // Verify the result
        assertEquals(expectedMessage, result);

        // Verify the interactions with the mocks
        verify(jwtCertValidator, times(1)).validateJWT(phoneNumberCertificateJwt);
        verify(bankAccountCertificateCreationService, times(1)).registerNewBankAccount(any(), any(), any(), anyString(), anyString());
    }



    @Test
    void testRegisterAccount_success_verifyBankAccountBO() {
        RegistrationRequest request = new RegistrationRequest();
        request.setPhoneNumber("1234567890");
        request.setPublicKey("publicKey123");
        String jwt = "validJwt";
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);

        when(redisTemplate.hasKey("1234567890")).thenReturn(false);
        when(jwtCertValidator.validateJWT(jwt)).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(bankAccountService.getAccountById(any())).thenReturn(new BankAccountBO());

        obsService.registerAccount(request, jwt);

        ArgumentCaptor<BankAccountBO> captor = ArgumentCaptor.forClass(BankAccountBO.class);
        verify(bankAccountCertificateCreationService).registerNewBankAccount(
                eq("1234567890"), eq("publicKey123"), captor.capture(), any(), any());

        BankAccountBO account = captor.getValue();
        assertEquals("1234567890", account.getMsisdn());
        assertEquals("XAF", account.getCurrency().getCurrencyCode());
        assertEquals("Standard", account.getProduct());
        assertEquals("72070032", account.getBic());
        assertEquals("OBS", account.getBranch());
    }

    @Test
    void testMakeTrans_success() {
        String accountId = "12345";
        when(bankAccountService.getAccountById(accountId)).thenReturn(new BankAccountBO());

        String result = obsService.makeTrans(accountId);

        assertEquals("5 transactions completed successfully for account " + accountId, result);
        verify(bankAccountTransactionService, times(5)).depositCash(eq(accountId), any(), any());
    }

    @Test
    void testMakeTrans_accountNotFound() {
        String accountId = "nonExistent";
        when(bankAccountService.getAccountById(accountId)).thenReturn(null);

        String result = obsService.makeTrans(accountId);

        assertEquals("Bank account not found for ID: " + accountId, result);
        verifyNoInteractions(bankAccountTransactionService);
    }
}