package com.adorsys.webank.obs.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

@RestController
@RequestMapping("/api")
public class Host {

    @GetMapping("/host")
    public String getApplicationUrl(HttpServletRequest request) {
        // Construct the URL
        StringBuilder sb = new StringBuilder();
        sb.append("scheme: ").append(request.getScheme()).append("\n"); // http or https
        sb.append("serverName: ").append(request.getServerName()).append("\n"); // server name
        sb.append("serverPort: ").append(request.getServerPort()).append("\n"); // server port
        sb.append("contextPath: ").append(request.getContextPath()).append("\n"); // application context path (if any)

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append(headerName).append(": ");

            Enumeration<String> headers = request.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                sb.append(headers.nextElement()).append(" ");
            }
            sb.append("\n");
        }

        // Format the URL
        return sb.toString();
    }
}
