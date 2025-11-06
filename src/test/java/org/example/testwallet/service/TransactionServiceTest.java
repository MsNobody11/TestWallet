package org.example.testwallet.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.example.testwallet.dao.entity.Wallet;
import org.example.testwallet.dao.enums.Currency;
import org.example.testwallet.dao.enums.OperationType;
import org.example.testwallet.dto.response.TransactionResponse;
import org.example.testwallet.repository.TransactionRepository;
import org.example.testwallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

  @Mock
  private WalletRepository walletRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionService transactionService;

  @Test
  void shouldDepositMoneySuccessfully() {
    // Given
    UUID walletId = UUID.randomUUID();
    Wallet wallet = Wallet.builder()
        .id(walletId)
        .balance(new BigDecimal("5000"))
        .currency(Currency.RUB)
        .ownerName("John Smith")
        .build();

    // Настраиваем мок
    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
    when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

    // When
    TransactionResponse response = transactionService.createTransaction(
        walletId,
        OperationType.DEPOSIT,
        BigDecimal.valueOf(1000)
    );

    // Then
    assertTrue(response.isSuccess());
    assertEquals(BigDecimal.valueOf(6000), response.getNewBalance());

    // Проверяем что методы вызвались
    verify(walletRepository).findById(walletId);
    verify(walletRepository).save(any(Wallet.class));
  }

  @Test
  void shouldFailWhenWalletNotFound() {
    // Given
    UUID walletId = UUID.randomUUID();
    when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

    // When
    TransactionResponse response = transactionService.createTransaction(
        walletId,
        OperationType.DEPOSIT,
        BigDecimal.valueOf(1000)
    );

    // Then
    assertFalse(response.isSuccess());
    assertTrue(response.getMessage().contains("Wallet not found"));
  }


}
