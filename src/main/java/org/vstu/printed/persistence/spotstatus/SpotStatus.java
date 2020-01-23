package org.vstu.printed.persistence.spotstatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SpotStatuses")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class SpotStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private final short id;

  @Column(name = "Status")
  private String status;
}
