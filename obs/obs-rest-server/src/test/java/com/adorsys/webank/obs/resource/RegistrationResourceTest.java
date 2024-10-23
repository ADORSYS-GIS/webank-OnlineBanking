//package com.adorsys.webank.obs.resource;
//
//import com.adorsys.webank.obs.service.RegistrationServiceApi;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class RegistrationResourceTest {
//
//    @InjectMocks
//    private RegistrationResource registrationResource;
//
//    @Mock
//    private RegistrationServiceApi registrationService;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(registrationResource).build();
//    }
//
//    @Test
//    public void testRegisterAccountSuccess() throws Exception {
//        // Arrange
//        String phoneNumber = "1234567890";
//        String publicKey = "dummyPublicKey";
//        String expectedResponse = "Registration successful"; // Adjust according to your service response
//        when(registrationService.registerAccount(phoneNumber, publicKey)).thenReturn(expectedResponse);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/registration")
//                        .param("phoneNumber", phoneNumber)
//                        .param("publicKey", publicKey))
//                .andExpect(status().isOk())
//                .andExpect(content().string(expectedResponse));
//    }
//
//    @Test
//    public void testRegisterAccountServiceThrowsException() throws Exception {
//        // Arrange
//        String phoneNumber = "1234567890";
//        String publicKey = "dummyPublicKey";
//        when(registrationService.registerAccount(phoneNumber, publicKey)).thenThrow(new RuntimeException("Service error"));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/registration")
//                        .param("phoneNumber", phoneNumber)
//                        .param("publicKey", publicKey))
//                .andExpect(status().isInternalServerError());
//    }

//    @Test
//    public void testRegisterAccountInvalidInput() throws Exception {
//        // Arrange
//        String phoneNumber = ""; // Invalid input (empty phone number)
//        String publicKey = "dummyPublicKey";
//        when(registrationService.registerAccount(phoneNumber, publicKey)).thenThrow(new InvalidInputException("Invalid input"));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/registration")
//                        .param("phoneNumber", phoneNumber)
//                        .param("publicKey", publicKey))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Registration failed: Invalid input")); // You might need to adjust this
//    }
//}
