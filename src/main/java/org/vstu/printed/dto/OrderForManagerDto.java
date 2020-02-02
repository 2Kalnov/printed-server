package org.vstu.printed.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderForManagerDto {
  private int id;
  private double cost;
  private Date createdAt;
  private Date doneAt;
  private Date receivedAt;
  private String receiveOption;
  private String status;
  private Integer clientId;
  private Integer spotId;
}
