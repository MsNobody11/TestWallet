package org.example.testwallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.testwallet.dao.enums.OperationType;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class TransactionRequest {

  @NotNull(message = "Wallet ID is required")
  private UUID walletId;

  @NotNull(message = "Operation type is required")
  private OperationType operationType;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
  private BigDecimal amount;

}
