package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vstu.printed.service.account.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AccountController {
  @Autowired
  private AccountService accountService;


}
