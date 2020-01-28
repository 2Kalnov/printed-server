package org.vstu.printed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
  private int id;
  private String name;
  private String email;
  private String phoneNumber;
  private int accountNumber;
}
