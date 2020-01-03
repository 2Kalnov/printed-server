package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vstu.printed.persistence.account.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
