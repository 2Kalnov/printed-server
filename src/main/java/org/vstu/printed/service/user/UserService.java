package org.vstu.printed.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.dto.signup.UserRegisterDto;
import org.vstu.printed.persistence.account.Account;
import org.vstu.printed.persistence.role.Role;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.repository.RoleRepository;
import org.vstu.printed.repository.UserRepository;
import org.vstu.printed.service.account.AccountService;
import org.vstu.printed.service.user.verify.PhoneNumberAlreadyVerified;
import org.vstu.printed.service.user.verify.VerificationCodeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("printedUserService")
@RequiredArgsConstructor
public class UserService {
  private final PasswordEncoder encoder;

  private final UserRepository repository;
  private final RoleRepository roleRepository;
  private final AccountService accountService;
  private final VerificationCodeService verificationCodeService;

  public UserDto getUserById(int id) {
    UserDto userDto;
    Optional<User> user = repository.findByIdNative(id);
    return userDto = user.map(this::mapToDto).orElse(null);
  }

  public List<UserDto> getAllUsers() {
    return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public void save(User user) {
    repository.insert(user.getEmail(), encoder.encode(user.getPassword()), user.getPhoneNumber(), user.getAccountNumber(), user.getRole().getId(), user.getName());
  }

  public User register(UserRegisterDto userInfo) throws DuplicateUserException, PhoneNumberAlreadyVerified {
    User user = createUserFromRegisterDto(userInfo);

    Account newAccount = new Account();
    newAccount.setCardNumberCut(userInfo.getCardNumberCut());
    newAccount.setBalance(BigDecimal.ZERO);
    newAccount.setRememberCard(false);

    Account savedAccount = accountService.saveAccount(newAccount);
    user.setAccountNumber(savedAccount.getNumber());

    // Генерация кода подтверждения аккаунта

    verificationCodeService.addCodeForNumber(userInfo.getPhoneNumber());

    User registeredUser = repository.save(user);

    return registeredUser;
  }

  public User findByPhoneNumber(String userInfo) throws UsernameNotFoundException {
    return repository.findByPhoneNumber(userInfo);
  }

  public void updatePhoneNumber(String phoneNumber, int userId) {
    repository.updatePhoneNumber(phoneNumber, userId);
  }

  public void updateEmail(String email, int userId) {
    repository.updateEmail(email, userId);
  }

  private User createUserFromRegisterDto(UserRegisterDto userRegisterDto) throws DuplicateUserException {
    User userWithSamePhoneNumber = repository.findByPhoneNumber(userRegisterDto.getPhoneNumber());
    User userWithSameEmail = repository.findByEmail(userRegisterDto.getEmail());

    if(userWithSameEmail == null && userWithSamePhoneNumber == null) {
      User user = new User(
              userRegisterDto.getName(),
              userRegisterDto.getEmail(),
              userRegisterDto.getPhoneNumber()
      );

      user.setPassword(encoder.encode(userRegisterDto.getPassword()));

      Optional<Role> foundUserRole = roleRepository.findByNameNative(userRegisterDto.getRoleName());
      Role userRole = foundUserRole.orElse(null);
      user.setRole(userRole);

      return user;
    }
    else
      throw new DuplicateUserException("User with the same phone number or email already exists.");
  }

  private UserDto mapToDto(User user) {
    UserDto userDto = new UserDto();

    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setAccountNumber(user.getAccountNumber());
    userDto.setEmail(user.getEmail());
    userDto.setPhoneNumber(user.getPhoneNumber());

    return userDto;
  }

}
