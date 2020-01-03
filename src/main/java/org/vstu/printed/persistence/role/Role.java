package org.vstu.printed.persistence.role;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "Roles")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class Role implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final short id;

  private final String name;

  @Override
  public String getAuthority() {
    return this.name;
  }
}
