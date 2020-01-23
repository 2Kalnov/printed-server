package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.persistence.account.Account;

import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  @Modifying
  @Transactional
  @Query(value = "insert Accounts values(0, 0, :cardNumberCut, null)", nativeQuery = true)
  void insertNative(
          @Param("cardNumberCut") String cardNumberCut
  );

  @Modifying
  @Transactional
  @Query(value = "update Accounts set RememberCard = :rememberCard, Balance = Balance - :balanceChange, CardNumberCut = :cardNumberCut where Number = :accountNumber", nativeQuery = true)
  void updateAccount(
          @Param("rememberCard") boolean rememberCard,
          @Param("balanceChange") BigDecimal balanceChange,
          @Param("cardNumberCut") String cardNumberCut,
          @Param("accountNumber") int accountNumber
  );
}
