package org.vstu.printed.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
  private String name;
  private String phoneNumber;
  private String email;
  private String password;
  private String roleName;
  private String cardNumberCut;
}
