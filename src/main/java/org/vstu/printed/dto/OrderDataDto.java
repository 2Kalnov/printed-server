package org.vstu.printed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDataDto {
  @JsonProperty(required = true)
  private double[] location;
  private int radius;
  private String receiveOption;
}
