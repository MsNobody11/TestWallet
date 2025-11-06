package org.example.testwallet.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.testwallet.dao.entity.Wallet;
import org.example.testwallet.dto.response.WalletBalanceResponse;
import org.example.testwallet.repository.TransactionRepository;
import org.example.testwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

  private final WalletRepository walletRepository;

  public WalletBalanceResponse getBalance(UUID id) {
    Wallet wallet = walletRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Wallet not found with id: " + id));

    return new WalletBalanceResponse(wallet.getId(),
          wallet.getBalance(),
          wallet.getCurrency(),
          wallet.getOwnerName());
  }



}
