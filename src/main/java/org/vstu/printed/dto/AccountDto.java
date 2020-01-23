package org.vstu.printed.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
  private BigDecimal balance;
  private Boolean rememberCard;
  private String cardNumberCut;
}
