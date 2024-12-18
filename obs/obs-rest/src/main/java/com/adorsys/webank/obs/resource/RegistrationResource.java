package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationResource implements RegistrationResourceApi {

    @Autowired
    private RegistrationServiceApi registrationService;
    @Autowired
    private HttpServletRequest request;
    @Override
    @PostMapping
    public ResponseEntity<String> registerAccount(@RequestBody RegistrationRequest registrationRequest) {
        System.out.println(request.getRequestURL());
        try {
            String result = registrationService.registerAccount(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            // Log the exception (optional)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }
}
