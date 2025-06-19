package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.config.SecurityUtils;
import com.adorsys.webank.exception.InvalidJwtException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        request.setAmount(new BigDecimal("200.00"));

        String accountCertJwt = "invalid-transaction-jwt";

        // Mock SecurityUtils.getCurrentUserJWT()
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserJWT).thenReturn(Optional.of(accountCertJwt));

            // Mock validator to return false for invalid JWT
            when(signTransactionValidator.validateSignTransactionJWT(accountCertJwt)).thenReturn(false);

            // Act & Assert
            assertThrows(InvalidJwtException.class, () -> withdrawService.withdraw(request));
            verify(signTransactionValidator, times(1)).validateSignTransactionJWT(accountCertJwt);
            verifyNoInteractions(transactionHelper);
        }
    }

    @Test
    void testWithdrawFailsDueToMissingJWT() {
        // Arrange
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderABC");
        request.setRecipientAccountId("recipientXYZ");
        request.setAmount(new BigDecimal("200.00"));

        // Mock SecurityUtils.getCurrentUserJWT() to return empty
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserJWT).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ServiceUnavailableException.class, () -> withdrawService.withdraw(request));
            verifyNoInteractions(signTransactionValidator);
            verifyNoInteractions(transactionHelper);
        }
    }

    @Test
    void testWithdrawSuccess() {
        // Arrange
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderDEF");
        request.setRecipientAccountId("recipientUVW");
        request.setAmount(new BigDecimal("300.00"));

        String accountCertJwt = "valid-transaction-jwt";
        String expectedTransactionId = "transactionCertString Success";

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
            ).thenReturn(expectedTransactionId);

            // Act
            MoneyTransferResponse response = withdrawService.withdraw(request);

            // Assert
            verify(signTransactionValidator, times(1)).validateSignTransactionJWT(accountCertJwt);
            verify(transactionHelper, times(1)).validateAndProcessTransaction(
                    eq("senderDEF"),
                    eq("recipientUVW"),
                    eq("300.00"),
                    eq(accountCertJwt),
                    any()
            );
            assertEquals(MoneyTransferResponse.TransferStatus.COMPLETED, response.getStatus());
            assertEquals(expectedTransactionId, response.getTransactionId());
            assertEquals(new BigDecimal("300.00"), response.getAmount());
            assertEquals("XAF", response.getCurrency());
            assertNotNull(response.getTimestamp());
            assertEquals("Withdrawal completed successfully", response.getMessage());
        }
    }

    @Test
    void testWithdrawThrowsServiceUnavailableExceptionOnUnexpectedError() {
        // Arrange
        MoneyTransferRequestDto request = new MoneyTransferRequestDto();
        request.setSenderAccountId("senderERR");
        request.setRecipientAccountId("recipientERR");
        request.setAmount(new BigDecimal("400.00"));
        String accountCertJwt = "valid-jwt";

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserJWT).thenReturn(Optional.of(accountCertJwt));
            when(signTransactionValidator.validateSignTransactionJWT(accountCertJwt)).thenReturn(true);
            when(transactionHelper.validateAndProcessTransaction(
                eq("senderERR"),
                eq("recipientERR"),
                eq("400.00"),
                eq(accountCertJwt),
                any()
            )).thenThrow(new RuntimeException("Unexpected error"));

            // Act & Assert
            assertThrows(ServiceUnavailableException.class, () -> withdrawService.withdraw(request));
        }
    }
}