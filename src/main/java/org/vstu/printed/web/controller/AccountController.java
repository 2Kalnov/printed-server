package org.vstu.printed.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.AccountDto;
import org.vstu.printed.dto.AccountUpdatingDataDto;
import org.vstu.printed.security.jwt.JwtUser;
import org.vstu.printed.service.account.AccountService;
import org.vstu.printed.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;
  private final UserService userService;

  @GetMapping("/accounts/{accountId}")
  public ResponseEntity<AccountDto> getAccountData(@PathVariable int accountId) {
    try {
      AccountDto accountInfo = accountService.getAccount(accountId);
      return ResponseEntity.ok(accountInfo);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PatchMapping("/users/{userId}/account")
  public ResponseEntity updateAccount(@RequestBody AccountUpdatingDataDto patchData, @PathVariable int userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object userData = authentication.getPrincipal();
    JwtUser userInfo = (JwtUser)(authentication.getPrincipal());
    int authenticatedUserId = userInfo.getId();

    if(authenticatedUserId != userId)
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    else {
      int accountId = userService.getUserById(authenticatedUserId).getAccountNumber();
      try {
        accountService.updateAccount(patchData, accountId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      } catch(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
    }
  }
}
