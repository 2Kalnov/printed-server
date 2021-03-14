package org.vstu.printed.persistence.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SignupCodes")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class VerificationCode {
  @Id
  @Column(name = "PhoneNumber")
  private String phoneNumber;

  /**
   * Четырёхзначный код подтверждения
   */
  @Column(name = "VerificationCode")
  private String verificationCode;

  @Column(name = "Verified")
  private boolean isVerified;
}
