package org.example.testwallet.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.testwallet.dao.entity.Transaction;
import org.example.testwallet.dao.entity.Wallet;
import org.example.testwallet.dao.enums.OperationType;
import org.example.testwallet.dto.response.TransactionResponse;
import org.example.testwallet.repository.TransactionRepository;
import org.example.testwallet.repository.WalletRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final WalletRepository walletRepository;

  private final TransactionRepository transactionRepository;

  @Retryable(
      value = ObjectOptimisticLockingFailureException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 100, multiplier = 2)  // 100ms, 200ms, 400ms
  )
  @Transactional
  public TransactionResponse createTransaction(UUID walletId,
      OperationType operationType, BigDecimal amount) {

    try {
      Wallet wallet = walletRepository.findById(walletId)
          .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

      BigDecimal oldBalance = wallet.getBalance();
      BigDecimal newBalance;


      if (operationType == OperationType.DEPOSIT) {
        // Пополнение
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
          throw new IllegalArgumentException("Deposit amount must be positive");
        }

        newBalance = oldBalance.add(amount);

      } else if (operationType == OperationType.WITHDRAW) {
        // Снятие
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
          throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (oldBalance.compareTo(amount) < 0) {
          throw new IllegalArgumentException("Insufficient funds");
        }
        newBalance = oldBalance.subtract(amount);

      } else {
        throw new IllegalArgumentException("Unknown operation type: " + operationType);
      }

      // Сохраняем обновленный кошелек
      wallet.setBalance(newBalance);
      walletRepository.save(wallet);

      // Создаем запись о транзакции
      Transaction transaction = Transaction.builder()
          .wallet(wallet)
          .amount(amount)
          .operationType(operationType)
          .balanceAfter(newBalance)
          .description("Operation: " + operationType)
          .build();

      transactionRepository.save(transaction);

      // Возвращаем успешный ответ
      return new TransactionResponse(
          walletId,
          newBalance,
          operationType + " operation completed successfully",
          true
      );

    } catch (IllegalArgumentException e) {
      // Возвращаем ответ с ошибкой
      return TransactionResponse.builder()
          .walletId(walletId)
          .message(e.getMessage())
          .success(false)
          .build();
    }
  }

    @Recover
    public TransactionResponse recover(ObjectOptimisticLockingFailureException e,
        UUID walletId, OperationType operationType, BigDecimal amount) {
      return new TransactionResponse(walletId, null,
          "Operation failed after multiple retries due to high load", false);
    }
}
