package org.vstu.printed.dto;

import lombok.Data;

@Data
public class SpotUpdatingDataDto {
  private final Double[] location;
  private final String address;
  private final String status;
}
