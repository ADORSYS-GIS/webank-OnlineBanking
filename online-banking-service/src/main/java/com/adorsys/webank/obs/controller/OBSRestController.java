package com.adorsys.webank.obs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OBSRestController {

    @GetMapping("/message")
    public String getMessage() {
        return "Hello from OBService!";
    }
}
