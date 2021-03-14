package org.vstu.printed.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.AuthenticationResponseDto;
import org.vstu.printed.dto.signup.UserRegisterDto;
import org.vstu.printed.dto.signup.verify.UserPhoneNumber;
import org.vstu.printed.dto.signup.verify.VerifySignupCodeDto;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.service.AuthenticationService;
import org.vstu.printed.service.user.*;
import org.vstu.printed.service.user.verify.InvalidCodeException;
import org.vstu.printed.service.user.verify.PhoneNumberAlreadyVerified;
import org.vstu.printed.service.user.verify.VerificationCodeService;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class RegisterController {
  private final UserService userService;
  private final AuthenticationService authService;
  private final VerificationCodeService verificationCodeService;

  @PostMapping
  public ResponseEntity<AuthenticationResponseDto> processRegistration(@RequestBody UserRegisterDto registrationData) {
    try {
      User user = userService.register(registrationData);

      return ResponseEntity.ok(authService.loginUser(user.getPhoneNumber(), registrationData.getPassword()));
    } catch(Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

  }

  @PostMapping("/verify")
  public ResponseEntity<String> verifySignup(@RequestBody VerifySignupCodeDto signupCode) {

    try {
      verificationCodeService.verifyCode(signupCode.getPhoneNumber(), signupCode.getCode());
    } catch(InvalidCodeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    return ResponseEntity.ok("Аккаунт подтверждён");
  }

  @GetMapping("/code/send")
  public ResponseEntity<String> sendCode(UserPhoneNumber request) {
    try {
      verificationCodeService.sendCode(request.getPhoneNumber());
    } catch (UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    return ResponseEntity.ok("Сообщение отправлено");
  }
}
