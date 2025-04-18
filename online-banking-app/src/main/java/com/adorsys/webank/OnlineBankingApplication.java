package com.adorsys.webank;

import com.adorsys.webank.obs.EnableObsServiceimpl;
import de.adorsys.ledgers.postings.impl.EnablePostingService;
import de.adorsys.webank.bank.api.service.BankAccountInitService;
import de.adorsys.webank.bank.api.service.EnableBankAccountService;
import de.adorsys.webank.bank.server.utils.client.ExchangeRateClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableBankAccountService
@EnablePostingService
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
@EnableObsServiceimpl
	public class OnlineBankingApplication implements ApplicationListener<ApplicationReadyEvent> {

		private final BankAccountInitService bankInitService;

		@Autowired
		public OnlineBankingApplication(BankAccountInitService bankInitService) {
			this.bankInitService = bankInitService;
		}

		public static void main(String[] args) {
			SpringApplication.run(OnlineBankingApplication.class, args);
		}

		@Override
		public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
			bankInitService.initConfigData();
		}
	}