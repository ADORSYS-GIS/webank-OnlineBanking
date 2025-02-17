package com.adorsys.webank.obs.repository;

import com.adorsys.webank.obs.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {
    List<FCMToken> findByUserId(String userId);
}
