package org.vstu.printed.dto;

import lombok.Data;

@Data
public class AccountUpdatingDataDto {
  private final Double balanceChange;
  private final String cardNumberCut;
  private final Boolean rememberCard;
}
