package org.vstu.printed.persistence.spot;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.vstu.printed.persistence.spotstatus.SpotStatus;
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
  private String name;

  @OneToOne
  @JoinColumn(name = "AdminId", referencedColumnName = "Id")
  private User admin;

  @Column(name = "Location")
  private byte[] location;

  @Column(name = "Address")
  private String address;

  @ManyToOne
  @JoinColumn(name = "StatusId", referencedColumnName = "Id")
  private SpotStatus status;
}
