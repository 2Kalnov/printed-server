package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.init.RepositoriesPopulatedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.UserRegisterDto;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.service.user.DuplicateUserException;
import org.vstu.printed.service.user.UserService;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class RegisterController {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserDto> processRegistration(@RequestBody UserRegisterDto registrationData) {
    try {
      UserDto user = userService.register(registrationData);
      return ResponseEntity.ok(user);
    } catch(DuplicateUserException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

  }
}
