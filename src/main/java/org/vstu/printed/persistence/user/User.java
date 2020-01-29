package org.vstu.printed.persistence.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.vstu.printed.persistence.account.Account;
import org.vstu.printed.persistence.role.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "Users")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int Id;

  @Column(name = "Name")
  private final String name;

  @Column(name = "Email")
  private final String email;

  @NotNull
  @Column(name = "Password", nullable = false)
  private String password;

  @NotNull
  @Column(name="PhoneNumber", nullable = false, unique = true)
  private final String phoneNumber;

  @NotNull
  @Column(name = "AccountNumber", nullable = false)
  private Integer accountNumber;

  @ManyToOne
  @JoinColumn(name = "Role", referencedColumnName = "Id")
  private Role role;

  /* Overriding UserDetails interface methods */

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
    SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(this.role.getAuthority());
    authorities.add(userAuthority);

    return authorities;
  }

  @Override
  public String getUsername() {
    return this.name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
