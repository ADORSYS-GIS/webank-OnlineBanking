package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.FCMMessageRequest;
import com.adorsys.webank.obs.entity.FCMToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Trans", description = "Operations related to Trans processing")
@RequestMapping("/api/notifications/")
public interface FCMRestApi {


    @Operation(summary = "Save a token for future use in sending notifications", description = "Saves a token-userId association")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved token successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request to save token account"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @PostMapping(value = "/save-token", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> saveToken(@RequestBody FCMToken request);

    @Operation(summary = "Send a notification", description = "Sends a notification using token-userId association")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request to send notification"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> sendNotification(@RequestBody FCMMessageRequest request);

}
