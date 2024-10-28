package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistrationServiceImplTest {

    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
    }

    @Test
    void registerAccount_ShouldReturnSuccessMessageWithPhoneNumber_WhenGivenValidInput() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        int testPhoneNumber = 123456789; // Set a valid phone number
        request.setPhoneNumber(testPhoneNumber);
        request.setPublicKey("validPublicKey"); // Set a valid public key

        // Act
        String result = registrationService.registerAccount(request);

        // Assert
        assertEquals("Registration successful for phone number: " + testPhoneNumber, result);
    }

    // You can add more tests here for edge cases, invalid input, etc.
}
