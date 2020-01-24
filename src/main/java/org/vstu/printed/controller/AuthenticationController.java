package org.vstu.printed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.dto.AuthenticationRequestDto;
import org.vstu.printed.security.jwt.JwtTokenProvider;
import org.vstu.printed.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserService userService;

  @Autowired
  public AuthenticationController(
          AuthenticationManager authManager,
          JwtTokenProvider tokenProvider,
          UserService userService
  ) {
    this.authenticationManager = authManager;
    this.tokenProvider = tokenProvider;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
    try {
      String phoneNumber = requestDto.getPhoneNumber();
      String password = requestDto.getPassword();
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
      User user = userService.findByPhoneNumber(phoneNumber);

      if(user == null)
        throw new UsernameNotFoundException("User with phone number " + phoneNumber + " does not exist");

      String token = tokenProvider.createToken(phoneNumber);

      Map<String, Object> response = new HashMap<>();
      response.put("phoneNumber", phoneNumber);
      response.put("token", token);
      response.put("id", user.getId());
      response.put("name", user.getName());
      response.put("email", user.getEmail());
      response.put("accountNumber", user.getAccountNumber());

      return ResponseEntity.ok(response);
    } catch(AuthenticationException e) {
      throw new BadCredentialsException("Invalid phone number or password");
    }
  }
}
