package com.adorsys.webank;

import com.adorsys.webank.obs.repository.FCMTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OnlineBankingApplicationTests {

    @MockBean
    private FCMTokenRepository fcmTokenRepository;

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}
