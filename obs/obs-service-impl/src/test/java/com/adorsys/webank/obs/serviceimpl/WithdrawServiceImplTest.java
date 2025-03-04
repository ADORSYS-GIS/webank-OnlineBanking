package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawServiceImplTest {

    @Mock
    private TransactionHelper transactionHelper;

    @Mock
    private SignTransactionJwtValidator signTransactionValidator;

    @InjectMocks
    private WithdrawServiceImpl withdrawService;

    @Test
    public void testWithdrawFailsDueToInvalidTransactionJWT() {
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderABC");
        request.setRecipientAccountId("recipientXYZ");
        request.setAmount("200.00");

        String accountCertJwt = "invalid-transaction-jwt";
        String expectedError = "Invalid transaction JWT";

        when(signTransactionValidator.validateSignTransactionJWT(accountCertJwt)).thenReturn(false);

        String response = withdrawService.withdraw(request, accountCertJwt);

        verify(signTransactionValidator, times(1)).validateSignTransactionJWT(accountCertJwt);
        verifyNoInteractions(transactionHelper);
        assertEquals(expectedError, response);
    }

    @Test
    public void testWithdrawSuccess() {
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderDEF");
        request.setRecipientAccountId("recipientUVW");
        request.setAmount("300.00");

        String accountCertJwt = "valid-transaction-jwt";
        String expectedResponse = "transactionCertString Success";

        when(signTransactionValidator.validateSignTransactionJWT(accountCertJwt)).thenReturn(true);
        when(transactionHelper.validateAndProcessTransaction(
                eq("senderDEF"),
                eq("recipientUVW"),
                eq("300.00"),
                eq(accountCertJwt),
                ArgumentMatchers.any())
        ).thenReturn(expectedResponse);

        String response = withdrawService.withdraw(request, accountCertJwt);

        verify(signTransactionValidator, times(1)).validateSignTransactionJWT(accountCertJwt);
        verify(transactionHelper, times(1)).validateAndProcessTransaction(
                eq("senderDEF"),
                eq("recipientUVW"),
                eq("300.00"),
                eq(accountCertJwt),
                ArgumentMatchers.any()
        );
        assertEquals(expectedResponse, response);
    }
}
