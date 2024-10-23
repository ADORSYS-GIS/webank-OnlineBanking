package com.adorsys.webank.obs.serviceimpl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationServiceImplTest {

    // Creating an instance of RegistrationServiceImpl to test the functionality
    private final RegistrationServiceImpl registrationService = new RegistrationServiceImpl();

    // Test case for a successful account registration
    @Test
    public void testRegisterAccountSuccess() {
        // Arrange: Setting up test data for phone number and public key
        String phoneNumber = "1234567890";
        String publicKey = "dummyPublicKey";

        // Act: Calling the registerAccount method to test its output
        String result = registrationService.registerAccount(phoneNumber, publicKey);

        // Assert: Verifying that the method returns the correct success message
        assertEquals("Successfully registered account for phone number: 1234567890", result);
        // This ensures the response from registerAccount matches the expected output for the given phone number

        // FOR NOW WE DON'T REALY HAVE A LOGIC TO TEST, WHEN THE LOGIC WILL BE IMPLEMENTED IN THE SERVICEIMPL SUCH AS FOWARDING THE REQUEST TO THE DAS MODULE, THEN WE WILL HANDLE THE TEST
    }
}
