package org.vstu.printed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class OrderDataDto {
  private double[] location;
  private int radius;

  @JsonProperty(required = true)
  private String receiveOption;
}
