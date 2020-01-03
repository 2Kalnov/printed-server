package org.vstu.printed.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vstu.printed.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository repository;


}
