package org.example.testwallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.example.testwallet.dao.entity.Wallet;
import org.example.testwallet.dao.enums.Currency;
import org.example.testwallet.dto.response.WalletBalanceResponse;
import org.example.testwallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

  @Mock
  WalletRepository walletRepository;

  @InjectMocks
  WalletService walletService;

  @Test
  void shouldReturnWalletBalanceWhenWalletExists() {
    // Given
    UUID walletId = UUID.randomUUID();
    Wallet wallet = Wallet.builder()
        .id(walletId)
        .balance(new BigDecimal("1500.50"))
        .currency(Currency.USD)
        .ownerName("Alice Smith")
        .build();

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

    // When
    WalletBalanceResponse response = walletService.getBalance(walletId);

    // Then
    assertNotNull(response);
    assertEquals(walletId, response.getWalletId());
    assertEquals(new BigDecimal("1500.50"), response.getBalance());
    assertEquals(Currency.USD, response.getCurrency());
    assertEquals("Alice Smith", response.getOwnerName());

    verify(walletRepository).findById(walletId);
  }

  @Test
  void shouldThrowExceptionWhenWalletNotFound() {
    // Given
    UUID walletId = UUID.randomUUID();
    when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> walletService.getBalance(walletId)
    );

    assertEquals("Wallet not found with id: " + walletId, exception.getMessage());
    verify(walletRepository).findById(walletId);
  }

  @Test
  void shouldReturnZeroBalanceForNewWallet() {
    // Given
    UUID walletId = UUID.randomUUID();
    Wallet wallet = Wallet.builder()
        .id(walletId)
        .balance(BigDecimal.ZERO)  // Новый кошелек
        .currency(Currency.RUB)
        .ownerName("New User")
        .build();

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

    // When
    WalletBalanceResponse response = walletService.getBalance(walletId);

    // Then
    assertEquals(BigDecimal.ZERO, response.getBalance());
    assertEquals("New User", response.getOwnerName());
  }

}
