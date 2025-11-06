package org.example.testwallet.controller;

import lombok.RequiredArgsConstructor;
import org.example.testwallet.dto.request.TransactionRequest;
import org.example.testwallet.dto.response.TransactionResponse;
import org.example.testwallet.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping("/wallet")
  public ResponseEntity<TransactionResponse> processTransactionOperation(
      @RequestBody TransactionRequest request) {

    TransactionResponse response = transactionService.createTransaction(
        request.getWalletId(),
        request.getOperationType(),
        request.getAmount()
    );

    // Если операция успешна - возвращаем 200 OK, иначе 400 Bad Request
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

}
