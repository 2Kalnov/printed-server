package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.dto.UserUpdatingDataDto;
import org.vstu.printed.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService service;

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable int id) {
    UserDto user = service.getUserById(id);
    if(user != null)
      return ResponseEntity.ok(user);
    else
      return ResponseEntity.notFound().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity updateUser(@RequestBody UserUpdatingDataDto patchData, @PathVariable int id) {
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

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable int id) {
    // При удалении необходимо также вручную удалять счёт пользователя из базы данных
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
