package org.vstu.printed.security.jwt;

import lombok.NoArgsConstructor;
import org.vstu.printed.persistence.user.User;

@NoArgsConstructor
public final class JwtUserFactory {
  public static JwtUser create(User user) {
    return new JwtUser(
            user.getId(),
            user.getPhoneNumber(),
            user.getPassword(),
            user.getEmail(),
            true,
            user.getAuthorities()
    );
  }
}
