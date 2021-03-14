package org.vstu.printed.service.user.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.vstu.printed.persistence.user.VerificationCode;
import org.vstu.printed.repository.VerificationCodeRepository;
import org.vstu.printed.service.sms.SmsSender;
import org.vstu.printed.service.user.UserNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class VerificationCodeServiceTest {

  private VerificationCodeService verificationCodeService;
  private VerificationCodeRepository verificationCodeRepository;
  private SmsSender smsSender;

  private final String CODE = "1111";
  private final String PHONE_NUMBER = "+79998881122";

  @BeforeEach
  void setUp() {
    verificationCodeRepository = Mockito.mock(VerificationCodeRepository.class);
    smsSender = Mockito.mock(SmsSender.class);

    verificationCodeService = new VerificationCodeService(verificationCodeRepository, smsSender);
  }

  @Test
  @DisplayName("Проверка кода с передачей корректого кода")
  void verifyValidCode() throws InvalidCodeException, UserNotFoundException {
    VerificationCode code = new VerificationCode();
    mockVerificationCode(code);

    Mockito.when(verificationCodeRepository.findByPhoneNumber(PHONE_NUMBER))
            .thenReturn(Optional.of(code));

    verificationCodeService.verifyCode(PHONE_NUMBER, CODE);

    ArgumentCaptor<VerificationCode> codeCaptor = ArgumentCaptor.forClass(VerificationCode.class);
    Mockito.verify(verificationCodeRepository).save(codeCaptor.capture());

    Assertions.assertTrue(codeCaptor.getValue().isVerified());
  }

  @Test
  @DisplayName("Проверка кода с передачей некорректного кода")
  void verifyInvalidCode() {
    VerificationCode code = new VerificationCode();
    mockVerificationCode(code);

    String invalidCode = "1122";

    Mockito.when(verificationCodeRepository.findByPhoneNumber(PHONE_NUMBER))
            .thenReturn(Optional.of(code));

    Assertions.assertThrows(InvalidCodeException.class, () ->
            verificationCodeService.verifyCode(PHONE_NUMBER, invalidCode));
  }

  @Test
  @DisplayName("Проверка созданного числового кода на уникальность с передачей несуществующего числового кода")
  void isSignupCodeUnique() {
    short code = 111;

    List<VerificationCode> codes = Arrays.asList(
            new VerificationCode("+79998881122", "0011", true),
            new VerificationCode("+79988892211", "1111", true),
            new VerificationCode("+78889993344", "1110", false)
    );

    Mockito.when(verificationCodeRepository.findAll())
            .thenReturn(codes);

    Assertions.assertTrue(verificationCodeService.isSignupCodeUnique(code));
  }

  @Test
  @DisplayName("Проверка созданного числового кода на уникальность с передачей уже существующего числового кода")
  void signupCodeIsNotUnique() {
    short code = 111;

    List<VerificationCode> codes = Arrays.asList(
            new VerificationCode("+79998881122", "0111", true),
            new VerificationCode("+79988892211", "1111", true),
            new VerificationCode("+78889993344", "1110", false)
    );

    Mockito.when(verificationCodeRepository.findAll())
            .thenReturn(codes);

    Assertions.assertFalse(verificationCodeService.isSignupCodeUnique(code));
  }

  @Test
  @DisplayName("Создание кода подтверждения регистрации для уже подтверждённого номера телефона")
  void getCodeForVerifiedPhoneNumber() {
    VerificationCode codeForNumber = new VerificationCode(PHONE_NUMBER, CODE, true);

    Mockito.when(verificationCodeRepository.findByPhoneNumber(PHONE_NUMBER))
            .thenReturn(Optional.of(codeForNumber));

    Assertions.assertThrows(PhoneNumberAlreadyVerified.class, () ->
            verificationCodeService.addCodeForNumber(PHONE_NUMBER));
  }

  private void mockVerificationCode(VerificationCode emptyCode) {
    emptyCode.setVerified(false);
    emptyCode.setPhoneNumber(PHONE_NUMBER);
    emptyCode.setVerificationCode(CODE);
  }
}