package org.example.testwallet.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.example.testwallet.dao.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

  Optional<Wallet> findById(UUID id);

}
