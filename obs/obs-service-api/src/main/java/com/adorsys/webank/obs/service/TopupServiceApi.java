package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.TopupRequestDto;

public interface TopupServiceApi {
    String topup(TopupRequestDto topupRequestDto);
} 