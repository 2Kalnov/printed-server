package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.service.user.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @GetMapping(value = "/users/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUser(@PathVariable int id) {
    return service.getUserById(id);
  }
}
