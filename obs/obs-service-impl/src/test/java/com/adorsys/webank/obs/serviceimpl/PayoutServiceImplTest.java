package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayoutServiceImplTest {

    @Mock
    private TransactionHelper transactionHelper;

    @InjectMocks
    private PayoutServiceImpl payoutService;

    @Test
    void testPayoutSuccess() {
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("sender123");
        request.setRecipientAccountId("recipient456");
        request.setAmount("150.00");

        String accountCertJwt = "valid-certificate-jwt";
        String expectedResponse = "transactionCertString Success";

        when(transactionHelper.validateAndProcessTransaction(
                eq("sender123"),
                eq("recipient456"),
                eq("150.00"),
                eq(accountCertJwt),
                ArgumentMatchers.any())
        ).thenReturn(expectedResponse);

        String response = payoutService.payout(request, accountCertJwt);

        verify(transactionHelper, times(1)).validateAndProcessTransaction(
                eq("sender123"),
                eq("recipient456"),
                eq("150.00"),
                eq(accountCertJwt),
                ArgumentMatchers.any()
        );
        assertEquals(expectedResponse, response);
    }

    @Test
    void testPayoutWhenHelperReturnsError() {
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("sender789");
        request.setRecipientAccountId("recipient012");
        request.setAmount("50.00");

        String accountCertJwt = "invalid-certificate-jwt";
        String expectedError = "Invalid certificate or JWT. Payout Request failed";

        when(transactionHelper.validateAndProcessTransaction(
                eq("sender789"),
                eq("recipient012"),
                eq("50.00"),
                eq(accountCertJwt),
                ArgumentMatchers.any())
        ).thenReturn(expectedError);

        String response = payoutService.payout(request, accountCertJwt);
        verify(transactionHelper, times(1)).validateAndProcessTransaction(
                eq("sender789"),
                eq("recipient012"),
                eq("50.00"),
                eq(accountCertJwt),
                ArgumentMatchers.any()
        );
        assertEquals(expectedError, response);
    }
}
