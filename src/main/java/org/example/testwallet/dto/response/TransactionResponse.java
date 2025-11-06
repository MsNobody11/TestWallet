package org.example.testwallet.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TransactionResponse {
  private UUID walletId;
  private BigDecimal newBalance;
  private String message;
  private boolean success;
}
