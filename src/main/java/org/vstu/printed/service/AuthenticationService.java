package org.vstu.printed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.AuthenticationResponseDto;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.security.jwt.JwtTokenProvider;
import org.vstu.printed.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @Value("${jwt.token.expire}")
  private static long expirationTime;

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserService userService;

  public AuthenticationResponseDto loginUser(String phoneNumber, String password) throws AuthenticationException {

      User user = authenticateUser(phoneNumber, password);

      String token = tokenProvider.createToken(phoneNumber);

      AuthenticationResponseDto authData = new AuthenticationResponseDto();
      authData.setPhoneNumber(phoneNumber);
      authData.setToken(token);
      authData.setId(user.getId());
      authData.setName(user.getName());
      authData.setEmail(user.getEmail());
      authData.setAccountNumber(user.getAccountNumber());
      authData.setExpire(expirationTime);
      return authData;
  }

  public User authenticateUser(String phoneNumber, String password) throws UsernameNotFoundException {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
    User user = userService.findByPhoneNumber(phoneNumber);

    if(user == null)
      throw new UsernameNotFoundException("User with phone number " + phoneNumber + " does not exist");

    return user;
  }

  public String authenticateWithToken(String phoneNumber, String password) {
    authenticateUser(phoneNumber, password);
    return tokenProvider.createToken(phoneNumber);
  }
}
