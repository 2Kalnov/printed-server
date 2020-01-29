package org.vstu.printed.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderUpdatingDataDto {
  private String status;
  private Integer spotId;
  private Integer radius;
}
