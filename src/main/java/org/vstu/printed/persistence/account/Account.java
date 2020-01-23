package org.vstu.printed.persistence.account;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Accounts")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Number")
  private final int number;

  @Column(name = "Balance")
  private BigDecimal balance;

  @Column(name = "RememberCard")
  private boolean rememberCard;

  @Column(name = "CardNumberCut")
  private String cardNumberCut;

  @Column(name = "SavedPaymentMethodId")
  private String savedPaymentMethodId;
}
