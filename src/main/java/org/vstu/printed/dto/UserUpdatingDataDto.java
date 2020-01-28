package org.vstu.printed.dto;

import lombok.Data;

@Data
public class UserUpdatingDataDto {
  private final String email;
  private final String phoneNumber;
  private final String password;
}
