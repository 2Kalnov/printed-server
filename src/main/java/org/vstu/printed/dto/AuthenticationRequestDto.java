package org.vstu.printed.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
  private String phoneNumber;
  private String password;
}
