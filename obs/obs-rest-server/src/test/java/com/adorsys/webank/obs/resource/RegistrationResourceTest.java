package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RegistrationResourceTest {

    @InjectMocks
    private RegistrationResource registrationResource; // Class under test

    @Mock
    private RegistrationServiceApi registrationService; // Mocked dependency

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerAccount_ShouldReturnSuccessfulResponse() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPhoneNumber("1234567890");
        registrationRequest.setPublicKey("testPublicKey");

        String expectedResponse = "Registration successful";
        when(registrationService.registerAccount(registrationRequest)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> responseEntity = registrationResource.registerAccount(registrationRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
