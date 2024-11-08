/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.resource.mockbank;

@org.springframework.context.annotation.Configuration
public class Config {
    @org.springframework.context.annotation.Bean
    public de.adorsys.ledgers.bank.api.service.domain.ASPSPConfigSource configSource() {
        return new MockBankConfigSource();
    }

	@org.springframework.context.annotation.Bean
	public java.security.Principal getPrincipal(){
		return () -> "anonymous";
	}
	
	@org.springframework.context.annotation.Bean
	public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
		return new com.fasterxml.jackson.databind.ObjectMapper()
				.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
	}
}
