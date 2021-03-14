package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vstu.printed.persistence.user.VerificationCode;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
  Optional<VerificationCode> findByPhoneNumber(String phoneNumber);
}
