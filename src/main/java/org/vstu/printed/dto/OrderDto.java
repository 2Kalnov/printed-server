package org.vstu.printed.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDto {
  private int id;
  private double cost;
  private Date createdAt;
  private Date doneAt;
  private Date receivedAt;
  private String receiveOption;
  private String status;
  private Integer spotId;
}
