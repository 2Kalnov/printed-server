package org.vstu.printed.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.AccountDto;
import org.vstu.printed.dto.AccountUpdatingDataDto;
import org.vstu.printed.persistence.account.Account;
import org.vstu.printed.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository repository;

  public AccountDto getAccount(int accountId) throws Exception {
    Optional<Account> foundAccount = repository.findById(accountId);
    if(foundAccount.isPresent())
      return mapToDto(foundAccount.get());
    else
      throw new Exception("Can not find such account");
  }

  public void createNewAccount(String cardNumber) throws Exception {
    String cardNumberCut = cardNumber.substring(12);
    repository.insertNative(cardNumberCut);
  }

  public Account saveAccount(Account account) {
    return repository.save(account);
  }

  public void updateAccount(AccountUpdatingDataDto patchData, int accountId) throws Exception {
    Optional<Account> foundAccount = repository.findById(accountId);
    if(foundAccount.isPresent()) {
      Account account = foundAccount.get();

      Boolean rememberCard = patchData.getRememberCard();
      String cardNumberCut = patchData.getCardNumberCut();
      BigDecimal balanceChange = new BigDecimal(patchData.getBalanceChange());

      if(rememberCard != null)
        account.setRememberCard(rememberCard);
      if(cardNumberCut != null)
        account.setCardNumberCut(cardNumberCut);
      if(balanceChange != null) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(balanceChange);
        account.setBalance(newBalance);
      }

      repository.save(account);

      //repository.updateAccount(rememberCard, balanceChange, cardNumberCut, accountId);
    }
    else
      throw new Exception("Can not update; did not find such an account");
  }

  private AccountDto mapToDto(Account account) {
    AccountDto dto = new AccountDto();
    dto.setBalance(account.getBalance());
    dto.setCardNumberCut(account.getCardNumberCut());
    dto.setRememberCard(account.isRememberCard());

    return dto;
  }
}
