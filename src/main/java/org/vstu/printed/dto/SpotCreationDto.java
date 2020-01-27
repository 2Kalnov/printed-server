package org.vstu.printed.dto;

import lombok.Data;

@Data
public class SpotCreationDto {
  private final String address;
  private final double[] location;
  private final String status;
}
