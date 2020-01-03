package org.vstu.printed.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.UserDto;
import org.vstu.printed.dto.UserRegisterDto;
import org.vstu.printed.persistence.account.Account;
import org.vstu.printed.persistence.role.Role;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.repository.AccountRepository;
import org.vstu.printed.repository.RoleRepository;
import org.vstu.printed.repository.UserRepository;

import java.util.Optional;

@Service("printedUserService")
@RequiredArgsConstructor
public class UserService {
  @Autowired
  private final PasswordEncoder encoder;

  @Autowired
  private final UserRepository repository;

  @Autowired
  private final RoleRepository roleRepository;

  @Autowired
  private final AccountRepository accountRepository;

  public ResponseEntity<UserDto> getUserById(int id) {
    ResponseEntity<UserDto> userDto;
    Optional<User> user = repository.findByIdNative(id);
    if(user.isPresent())
      userDto = ResponseEntity.ok(mapToDto(user.get()));
    else
      userDto = ResponseEntity.notFound().build();

    return userDto;
  }

  public void save(User user) {
    repository.insert(user.getEmail(), encoder.encode(user.getPassword()), user.getPhoneNumber(), user.getAccountNumber(), user.getRole().getId(), user.getName());
  }

  public UserDto register(UserRegisterDto userInfo) {
    User user = createUserFromRegisterDto(userInfo);

    Account newUserAccount = accountRepository.save(new Account());
    user.setAccountNumber(newUserAccount.getNumber());

    User registeredUser = repository.save(user);

    return mapToDto(registeredUser);
  }

  private User createUserFromRegisterDto(UserRegisterDto userRegisterDto) {
    User user = new User(
            userRegisterDto.getName(),
            userRegisterDto.getEmail(),
            userRegisterDto.getPhoneNumber()
    );

    user.setPassword(encoder.encode(userRegisterDto.getPassword()));

    Role userRole = roleRepository.findByNameNative(userRegisterDto.getName());
    user.setRole(userRole);

    return user;
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

  public User findByPhoneNumber(String userInfo) throws UsernameNotFoundException {
    return repository.findByPhoneNumber(userInfo);
  }

}
