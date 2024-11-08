//package com.adorsys.webank.obs.resource;
//
//import com.adorsys.webank.obs.dto.RegistrationRequest;
//import com.adorsys.webank.obs.service.RegistrationServiceApi;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RegistrationResourceTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RegistrationServiceApi registrationService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void shouldRegisterAccountSuccessfully() throws Exception {
//        // Arrange
//        RegistrationRequest request = new RegistrationRequest();
//        request.setPhoneNumber("1234567890");
//        request.setPublicKey("examplePublicKey");
//
//        String expectedMessage = "Registration successful for phone number: 1234567890. Account ID: abc123";
//        when(registrationService.registerAccount(request)).thenReturn(expectedMessage);
//
//        // Act
//        MvcResult result = mockMvc.perform(post("/api/registration")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())  // Expecting HTTP 201 Created
//                .andReturn();
//
//        // Log the response for debugging purposes
//        String actualResponse = result.getResponse().getContentAsString();
//        System.out.println("Response: " + actualResponse);  // Print response to console for debugging
//
//        // Assert
//        assertEquals(expectedMessage, actualResponse, "Response message should match expected message");
//    }
//}
