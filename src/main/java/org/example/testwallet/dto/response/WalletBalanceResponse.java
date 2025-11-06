package org.example.testwallet.dto.response;

import java.math.BigDecimal;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.testwallet.dao.enums.Currency;

@AllArgsConstructor
@Getter
public class WalletBalanceResponse {
  private UUID walletId;
  private BigDecimal balance;
  private Currency currency;
  private String ownerName;

}
