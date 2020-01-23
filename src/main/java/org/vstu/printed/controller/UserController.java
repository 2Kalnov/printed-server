package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.dto.UserUpdatingDataDto;
import org.vstu.printed.service.user.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @GetMapping(value = "/users/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUser(@PathVariable int id) {
    UserDto user = service.getUserById(id);
    if(user != null)
      return ResponseEntity.ok(user);
    else
      return ResponseEntity.notFound().build();
  }

  @PatchMapping(value = "/users/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity createUser(@RequestBody UserUpdatingDataDto patchData, @PathVariable int id) {
    try {
      boolean wasUpdated = service.updateUser(patchData, id);
      if(wasUpdated)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      else
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch(Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
