package org.vstu.printed.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDto {
  private String phoneNumber;
  private String token;
  private int id;
  private String name;
  private String email;
  private int accountNumber;
  private long expire;
}
