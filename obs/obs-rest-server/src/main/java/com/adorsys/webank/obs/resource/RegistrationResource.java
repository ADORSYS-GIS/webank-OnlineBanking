package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationResource implements RegistrationResourceApi {

    @Autowired
    private RegistrationServiceApi registrationService;

    @Override
    @PostMapping
    public ResponseEntity<String> registerAccount(@RequestBody RegistrationRequest registrationRequest) {
        String result = registrationService.registerAccount(registrationRequest);
        return ResponseEntity.ok(result);
    }
}
