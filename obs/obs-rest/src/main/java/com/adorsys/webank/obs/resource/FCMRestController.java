package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.FCMMessageRequest;
import com.adorsys.webank.obs.entity.FCMToken;
import com.adorsys.webank.obs.service.FCMService;
import com.adorsys.webank.obs.service.FCMTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FCMRestController implements FCMRestApi {

    private final FCMService fcmService;
    private final FCMTokenService fcmTokenService;

    public FCMRestController(FCMService fcmService, FCMTokenService fcmTokenService) {
        this.fcmService = fcmService;
        this.fcmTokenService = fcmTokenService;
    }

    @Override
    public ResponseEntity<String> sendNotification(@RequestBody FCMMessageRequest fcmMessageRequest) {

        String title = fcmMessageRequest.getTitle();
        String body = fcmMessageRequest.getBody();
        String token = fcmMessageRequest.getToken();

        fcmService.sendPushNotification(token, title, body);
        return ResponseEntity.ok("Notification sent successfully!");
    }

    @Override
    public ResponseEntity<String> saveToken(@RequestBody FCMToken FCMEntry) {
        String userId = FCMEntry.getUserId();
        String token = FCMEntry.getToken();

        if (userId == null || token == null) {
            return ResponseEntity.badRequest().body("User ID and token are required");
        }

        fcmTokenService.saveToken(userId, token);

        System.out.println("Saved FCM Token for user " + userId + ": " + token);

        return ResponseEntity.ok("Token saved successfully");
    }

}
