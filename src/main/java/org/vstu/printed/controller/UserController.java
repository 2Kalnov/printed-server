package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.dto.UserUpdatingDataDto;
import org.vstu.printed.service.AuthenticationService;
import org.vstu.printed.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService service;
  private final AuthenticationService authService;

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
    String phoneNumber = patchData.getPhoneNumber();
    String password = patchData.getPassword();
    String email = patchData.getEmail();

    try {
      if(email != null && !email.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty()) {
        service.updateEmail(email, id);
        service.updatePhoneNumber(phoneNumber, id);
        return ResponseEntity.ok(authService.authenticateWithToken(phoneNumber, password));
      }
      else if(phoneNumber != null && !phoneNumber.isEmpty()) {
        service.updatePhoneNumber(phoneNumber, id);
        return ResponseEntity.ok(authService.authenticateWithToken(phoneNumber, password));
      }
      else if(email != null && !email.isEmpty()) {
        service.updateEmail(email, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
      else
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch(Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> registeredUsers = service.getAllUsers();
    if(registeredUsers.size() > 0)
      return ResponseEntity.ok(registeredUsers);
    else
      return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable int id) {
    // При удалении необходимо также вручную удалять счёт пользователя из базы данных
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
