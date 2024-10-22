package com.adorsys.webank.obs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/api/obs")
public interface OBSRestApi {

    @GetMapping("/message")
    @ResponseBody
    String getMessage();
}

