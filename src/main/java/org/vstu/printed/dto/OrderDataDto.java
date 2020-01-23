package org.vstu.printed.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDataDto {
  private double cost;

  private double[] location;
  private int radius;

  private Date createdAt;
  private int userId;
  private String receiveOption;

}
