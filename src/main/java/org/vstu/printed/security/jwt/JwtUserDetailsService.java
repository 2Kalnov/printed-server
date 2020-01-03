package org.vstu.printed.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vstu.printed.persistence.user.User;
import org.vstu.printed.service.user.UserService;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String userInfo) throws UsernameNotFoundException {
    User user = userService.findByPhoneNumber(userInfo);
    if(user == null)
      throw new UsernameNotFoundException("User with phone number" + userInfo + "is not found");

    JwtUser jwtUser = JwtUserFactory.create(user);

    return jwtUser;
  }
}
