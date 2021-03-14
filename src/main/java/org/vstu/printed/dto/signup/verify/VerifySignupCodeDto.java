package org.vstu.printed.dto.signup.verify;

import lombok.Data;

@Data
public class VerifySignupCodeDto {
  private String code;
  private String phoneNumber;
}
