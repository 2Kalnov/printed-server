package org.vstu.printed.persistence.spot;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.vstu.printed.persistence.user.User;

@Entity
@Table(name = "Spots")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class Spot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  public final int Id;

  @Column(name = "Name")
  private final String name;

  @NotNull
  @OneToOne
  @JoinColumn(name = "AdminId", referencedColumnName = "Id")
  private final User admin;

  @Column(name = "Location")
  private final byte[] location;

  @Column(name = "Address")
  private final String address;
}
