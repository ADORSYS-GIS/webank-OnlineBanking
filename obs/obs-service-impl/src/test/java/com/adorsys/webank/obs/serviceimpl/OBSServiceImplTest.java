package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.serviceimpl.OBSServiceImpl;
import de.adorsys.ledgers.bank.api.domain.BankAccountBO;
import de.adorsys.ledgers.bank.api.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OBSServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private OBSServiceImpl obsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterAccount_Success() {
        // Prepare test data
        RegistrationRequest request = new RegistrationRequest();
        request.setPhoneNumber("123456789");

        BankAccountBO createdAccount = BankAccountBO.builder()
                .id(UUID.randomUUID().toString())
                .currency(Currency.getInstance("XAF"))
                .creditLimit(BigDecimal.ZERO)
                .build();

        // Configure mock behavior
        when(bankAccountService.createNewAccount(any(BankAccountBO.class), any(String.class), eq("OBS"))).thenReturn(createdAccount);

        // Run the test
        String result = obsService.registerAccount(request);

        // Verify and assert results
        assertEquals("Registration successful for phone number: 123456789. Account ID: " + createdAccount.getId(), result);
        verify(bankAccountService, times(1)).createNewAccount(any(BankAccountBO.class), any(String.class), eq("OBS"));
    }

    @Test
    void testRegisterAccount_Failure() {
        // Prepare test data
        RegistrationRequest request = new RegistrationRequest();
        request.setPhoneNumber("123456789");

        // Configure mock behavior to simulate failure
        when(bankAccountService.createNewAccount(any(BankAccountBO.class), any(String.class), eq("OBS"))).thenReturn(null);

        // Run the test
        String result = obsService.registerAccount(request);

        // Verify and assert results
        assertEquals("Error creating account for phone number: 123456789", result);
        verify(bankAccountService, times(1)).createNewAccount(any(BankAccountBO.class), any(String.class), eq("OBS"));
    }

    @Test
    void testRegisterAccount_Exception() {
        // Prepare test data
        RegistrationRequest request = new RegistrationRequest();
        request.setPhoneNumber("123456789");

        // Configure mock behavior to throw an exception
        when(bankAccountService.createNewAccount(any(BankAccountBO.class), any(String.class), eq("OBS")))
                .thenThrow(new RuntimeException("Test Exception"));

        // Run the test
        String result = obsService.registerAccount(request);

        // Verify and assert results
        assertEquals("An error occurred while processing the request: Test Exception", result);
        verify(bankAccountService, times(1)).createNewAccount(any(BankAccountBO.class), any(String.class), eq("OBS"));
    }
}
