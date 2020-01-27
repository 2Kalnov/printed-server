package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.AuthenticationResponseDto;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.dto.AuthenticationRequestDto;
import org.vstu.printed.security.jwt.JwtTokenProvider;
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
