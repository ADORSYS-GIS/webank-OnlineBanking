package com.adorsys.webank.obs.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class Host {

    @GetMapping("/host")
    public String getApplicationUrl(HttpServletRequest request) {
        // Construct the URL
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName(); // localhost or domain
        int serverPort = request.getServerPort(); // port number
        String contextPath = request.getContextPath(); // application context path (if any)

        // Format the URL
        return String.format("%s://%s:%d%s", scheme, serverName, serverPort, contextPath);
    }
}