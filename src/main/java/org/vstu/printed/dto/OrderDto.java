package org.vstu.printed.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDto {
  private double cost;
  private Date createdAt;
  private Date doneAt;
  private Date receivedAt;
  private String receiveOption;
  private String status;
  private int spotId;
}
