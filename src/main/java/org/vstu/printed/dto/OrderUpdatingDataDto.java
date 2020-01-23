package org.vstu.printed.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderUpdatingDataDto {
  private String status;
  private Date receivedAt;
  private Date doneAt;
  private Integer spotId;
  private Integer radius;
}
