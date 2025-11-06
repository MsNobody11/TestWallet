package org.example.testwallet.controller;


import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.testwallet.dto.response.WalletBalanceResponse;
import org.example.testwallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController {

  private final WalletService walletService;

  @GetMapping("/wallets/{walletId}")
  public ResponseEntity<WalletBalanceResponse> getBalanceWallet(@PathVariable UUID walletId) {

    WalletBalanceResponse walletBalanceResponse=walletService.getBalance(walletId);
    return ResponseEntity.ok()
        .body(walletBalanceResponse);
  }

}
