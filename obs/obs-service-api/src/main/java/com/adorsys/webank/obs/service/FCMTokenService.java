package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.entity.FCMToken;
import com.adorsys.webank.obs.repository.FCMTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FCMTokenService {

    private final FCMTokenRepository tokenRepository;

    public FCMTokenService(FCMTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(String userId, String token) {
        FCMToken fcmToken = new FCMToken();
        fcmToken.setUserId(userId);
        fcmToken.setToken(token);
        tokenRepository.save(fcmToken);
    }

    public List<FCMToken> getUserTokens(String userId) {
        return tokenRepository.findByUserId(userId);
    }
}
