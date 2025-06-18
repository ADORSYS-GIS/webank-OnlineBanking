package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.config.SecurityUtils;
import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawServiceImplTest {

    @Mock
    private TransactionHelper transactionHelper;

    @Mock
    private SignTransactionJwtValidator signTransactionValidator;

    @InjectMocks
    private WithdrawServiceImpl withdrawService;

    @Test
    void testWithdrawFailsDueToInvalidTransactionJWT() {
        // Arrange
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderABC");
        request.setRecipientAccountId("recipientXYZ");
        request.setAmount("200.00");

        String accountCertJwt = "invalid-transaction-jwt";

        // Mock SecurityUtils.getCurrentUserJWT()
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserJWT).thenReturn(Optional.of(accountCertJwt));

            // Act + Assert
            IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                    withdrawService.withdraw(request)
            );

            assertEquals("Invalid JWT token for transaction signing", exception.getMessage());;

            // Assert
            verify(signTransactionValidator, times(1)).validateSignTransactionJWT(accountCertJwt);
            verifyNoInteractions(transactionHelper);

        }
    }

    @Test
    void testWithdrawSuccess() {
        // Arrange
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderDEF");
        request.setRecipientAccountId("recipientUVW");
        request.setAmount("300.00");

        String accountCertJwt = "valid-transaction-jwt";
        String expectedResponse = "transactionCertString Success";

        // Mock SecurityUtils.getCurrentUserJWT()
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserJWT).thenReturn(Optional.of(accountCertJwt));

            // Mock validator to return true for valid JWT
            when(signTransactionValidator.validateSignTransactionJWT(accountCertJwt)).thenReturn(true);
            when(transactionHelper.validateAndProcessTransaction(
                    eq("senderDEF"),
                    eq("recipientUVW"),
                    eq("300.00"),
                    eq(accountCertJwt),
                    any())
            ).thenReturn(expectedResponse);

            // Act
            String response = withdrawService.withdraw(request);

            // Assert
            verify(signTransactionValidator, times(1)).validateSignTransactionJWT(accountCertJwt);
            verify(transactionHelper, times(1)).validateAndProcessTransaction(
                    eq("senderDEF"),
                    eq("recipientUVW"),
                    eq("300.00"),
                    eq(accountCertJwt),
                    any()
            );
            assertEquals(expectedResponse, response);
        }
    }
}