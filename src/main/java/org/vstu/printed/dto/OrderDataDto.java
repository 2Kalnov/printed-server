package org.vstu.printed.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDataDto {
  private double[] location;
  private int radius;
  private String receiveOption;
}
