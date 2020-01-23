package org.vstu.printed.persistence.receiveoption;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ReceiveOptions")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class ReceiveOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private final short id;

  @Column(name = "[Option]")
  private String option;
}
