package org.vstu.printed.web.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.AuthenticationResponseDto;
import org.vstu.printed.dto.AuthenticationRequestDto;
import org.vstu.printed.service.AuthenticationService;
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authService;

  @PostMapping
  public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
    try {
      AuthenticationResponseDto authData = authService.loginUser(
              requestDto.getPhoneNumber(),
              requestDto.getPassword()
      );

      return ResponseEntity.ok(authData);
    } catch(AuthenticationException e) {
      throw new BadCredentialsException("Invalid phone number or password");
    }
  }
}
