import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.adorsys.webank.obs.serviceimpl.RegistrationServiceImpl;

public class RegistrationServiceImplTest {

    private final RegistrationServiceImpl registrationService = new RegistrationServiceImpl();

    @Test
    public void testRegisterAccountSuccess() {
        // Arrange
        String phoneNumber = "1234567890";
        String publicKey = "dummyPublicKey";

        // Act
        String result = registrationService.registerAccount(phoneNumber, publicKey);

        // Assert
        assertEquals("Successfully registered account for phone number: 1234567890", result);
    }
}
