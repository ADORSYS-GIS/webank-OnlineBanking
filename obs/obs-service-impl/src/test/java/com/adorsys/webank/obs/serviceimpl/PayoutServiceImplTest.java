package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.JwtHeaderExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayoutServiceImplTest {

    @Mock
    private TransactionHelper transactionHelper;

    @InjectMocks
    private PayoutServiceImpl payoutService;

    private static final String VALID_JWT_TOKEN = "valid.jwt.token";
    private static final String VALID_ACCOUNT_CERT = "valid.account.cert";
    private static final String VALID_KYC_CERT = "valid.kyc.cert";
    private static final String SENDER_ACCOUNT_ID = "sender123";
    private static final String RECIPIENT_ACCOUNT_ID = "recipient456";
    private static final String TRANSACTION_SUCCESS = "transaction.cert Success";

    private MoneyTransferRequestDto smallAmountRequest;
    private MoneyTransferRequestDto largeAmountRequest;

    @BeforeEach
    void setUp() {
        // Setup small amount request (less than 1000)
        smallAmountRequest = new MoneyTransferRequestDto();
        smallAmountRequest.setSenderAccountId(SENDER_ACCOUNT_ID);
        smallAmountRequest.setRecipientAccountId(RECIPIENT_ACCOUNT_ID);
        smallAmountRequest.setAmount("500.00");

        // Setup large amount request (greater than 1000)
        largeAmountRequest = new MoneyTransferRequestDto();
        largeAmountRequest.setSenderAccountId(SENDER_ACCOUNT_ID);
        largeAmountRequest.setRecipientAccountId(RECIPIENT_ACCOUNT_ID);
        largeAmountRequest.setAmount("1500.00");
    }

    @Test
    void payout_withValidSmallAmountRequest_shouldSucceed() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(VALID_ACCOUNT_CERT);
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("kycCertJwt")))
                    .thenReturn(null);

            when(transactionHelper.validateAndProcessTransaction(
                    eq(SENDER_ACCOUNT_ID),
                    eq(RECIPIENT_ACCOUNT_ID),
                    eq("500.00"),
                    eq(VALID_JWT_TOKEN),
                    any(Logger.class)
            )).thenReturn(TRANSACTION_SUCCESS);

            // Act
            String result = payoutService.payout(smallAmountRequest);

            // Assert
            assertEquals(TRANSACTION_SUCCESS, result);
            verify(transactionHelper).validateAndProcessTransaction(
                    eq(SENDER_ACCOUNT_ID),
                    eq(RECIPIENT_ACCOUNT_ID),
                    eq("500.00"),
                    eq(VALID_JWT_TOKEN),
                    any(Logger.class)
            );
        }
    }

    @Test
    void payout_withValidLargeAmountRequestAndKycCert_shouldSucceed() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(VALID_ACCOUNT_CERT);
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("kycCertJwt")))
                    .thenReturn(VALID_KYC_CERT);

            when(transactionHelper.validateAndProcessTransaction(
                    eq(SENDER_ACCOUNT_ID),
                    eq(RECIPIENT_ACCOUNT_ID),
                    eq("1500.00"),
                    eq(VALID_JWT_TOKEN),
                    any(Logger.class)
            )).thenReturn(TRANSACTION_SUCCESS);

            // Act
            String result = payoutService.payout(largeAmountRequest);

            // Assert
            assertEquals(TRANSACTION_SUCCESS, result);
            verify(transactionHelper).validateAndProcessTransaction(
                    eq(SENDER_ACCOUNT_ID),
                    eq(RECIPIENT_ACCOUNT_ID),
                    eq("1500.00"),
                    eq(VALID_JWT_TOKEN),
                    any(Logger.class)
            );
        }
    }

    @Test
    void payout_withLargeAmountRequestWithoutKycCert_shouldThrowException() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(VALID_ACCOUNT_CERT);
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("kycCertJwt")))
                    .thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                payoutService.payout(largeAmountRequest);
            });

            assertEquals("KYC certificate is required for transactions exceeding 10,000 francs.", exception.getMessage());
            verify(transactionHelper, never()).validateAndProcessTransaction(anyString(), anyString(), anyString(), anyString(), any(Logger.class));
        }
    }

    @Test
    void payout_withoutAccountCert_shouldThrowException() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                payoutService.payout(smallAmountRequest);
            });

            assertEquals("Account certificate is required for all transactions.", exception.getMessage());
            verify(transactionHelper, never()).validateAndProcessTransaction(anyString(), anyString(), anyString(), anyString(), any(Logger.class));
        }
    }

    @Test
    void payout_withEmptyAccountCert_shouldThrowException() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn("");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                payoutService.payout(smallAmountRequest);
            });

            assertEquals("Account certificate is required for all transactions.", exception.getMessage());
            verify(transactionHelper, never()).validateAndProcessTransaction(anyString(), anyString(), anyString(), anyString(), any(Logger.class));
        }
    }

    @Test
    void payout_withNullJwtToken_shouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            payoutService.payout(smallAmountRequest);
        });
        verify(transactionHelper, never()).validateAndProcessTransaction(anyString(), anyString(), anyString(), anyString(), any(Logger.class));
    }

    @Test
    void payout_withInvalidAmountFormat_shouldThrowException() {
        // Arrange
        MoneyTransferRequestDto invalidAmountRequest = new MoneyTransferRequestDto();
        invalidAmountRequest.setSenderAccountId(SENDER_ACCOUNT_ID);
        invalidAmountRequest.setRecipientAccountId(RECIPIENT_ACCOUNT_ID);
        invalidAmountRequest.setAmount("not-a-number");

        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(VALID_ACCOUNT_CERT);

            // Act & Assert
            assertThrows(NumberFormatException.class, () -> {
                payoutService.payout(invalidAmountRequest);
            });
            verify(transactionHelper, never()).validateAndProcessTransaction(anyString(), anyString(), anyString(), anyString(), any(Logger.class));
        }
    }

    @Test
    void payout_transactionHelperThrowsException_shouldPropagateException() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(VALID_ACCOUNT_CERT);
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("kycCertJwt")))
                    .thenReturn(null);

            when(transactionHelper.validateAndProcessTransaction(
                    anyString(), anyString(), anyString(), anyString(), any(Logger.class)
            )).thenThrow(new RuntimeException("Transaction processing error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                payoutService.payout(smallAmountRequest);
            });

            assertEquals("Transaction processing error", exception.getMessage());
        }
    }

    @Test
    void payout_loggingFunctionality_shouldLogCorrectInformation() {
        // Arrange
        try (MockedStatic<JwtHeaderExtractor> extractorMock = mockStatic(JwtHeaderExtractor.class)) {
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("accountJwt")))
                    .thenReturn(VALID_ACCOUNT_CERT);
            extractorMock.when(() -> JwtHeaderExtractor.extractField(eq(VALID_JWT_TOKEN), eq("kycCertJwt")))
                    .thenReturn(VALID_KYC_CERT);

            when(transactionHelper.validateAndProcessTransaction(
                    anyString(), anyString(), anyString(), anyString(), any(Logger.class)
            )).thenReturn(TRANSACTION_SUCCESS);

            // Use ArgumentCaptor to verify logging parameters passed to TransactionHelper
            ArgumentCaptor<Logger> loggerCaptor = ArgumentCaptor.forClass(Logger.class);

            // Act
            payoutService.payout(smallAmountRequest);

            // Assert
            verify(transactionHelper).validateAndProcessTransaction(
                    anyString(), anyString(), anyString(), anyString(), loggerCaptor.capture()
            );

            Logger capturedLogger = loggerCaptor.getValue();
            assertNotNull(capturedLogger);
            assertEquals(PayoutServiceImpl.class.getName(), capturedLogger.getName());
        }
    }
}