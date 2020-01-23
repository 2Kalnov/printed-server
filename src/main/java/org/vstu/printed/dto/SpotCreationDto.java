package org.vstu.printed.dto;

import lombok.Data;

@Data
public class SpotCreationDto {
  private int adminId;
  private String address;
  private double[] location;
  private String status;
}
