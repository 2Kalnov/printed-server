package org.vstu.printed.persistence.orderstatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "OrderStatuses")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class OrderStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private final short id;

  @Column(name = "Status")
  private String status;
}
