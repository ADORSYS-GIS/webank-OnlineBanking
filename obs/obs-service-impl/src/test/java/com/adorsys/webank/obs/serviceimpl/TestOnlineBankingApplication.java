package com.adorsys.webank.obs.serviceimpl;

import de.adorsys.ledgers.bank.api.service.BankAccountInitService;
import de.adorsys.ledgers.bank.api.service.EnableBankAccountService;
import de.adorsys.ledgers.bank.server.utils.client.ExchangeRateClient;
import de.adorsys.ledgers.postings.impl.EnablePostingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
@EnableBankAccountService
@EnablePostingService
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)


public class TestOnlineBankingApplication implements ApplicationListener<ApplicationReadyEvent> {
	private final BankAccountInitService bankInitService;


	@Autowired
	public TestOnlineBankingApplication(BankAccountInitService bankInitService) {
		this.bankInitService = bankInitService;
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestOnlineBankingApplication.class).run(args);
	}

	@Override
	public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
		bankInitService.initConfigData();
	}
}