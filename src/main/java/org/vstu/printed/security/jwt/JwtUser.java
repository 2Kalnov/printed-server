package org.vstu.printed.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class JwtUser implements UserDetails {

  private final int id;
  private final String phoneNumber;
  private final String password;
  private final String email;
  private final boolean enabled;

  private final Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  public int getId() {
    return this.id;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.phoneNumber;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    boolean enabled = true;
    for(GrantedAuthority authority : authorities) {
      if(authority.getAuthority().equals("banned"))
        enabled = false;
    }

    return enabled;
  }
}
