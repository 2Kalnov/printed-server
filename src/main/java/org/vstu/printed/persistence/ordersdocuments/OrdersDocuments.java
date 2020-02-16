package org.vstu.printed.persistence.ordersdocuments;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "OrdersDocuments")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class OrdersDocuments {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "OrderId")
  private final int orderId;

  @Column(name = "DocumentId")
  private final int documentId;

  @Column(name = "Colorful")
  private boolean isColorful = false;

  @Column(name = "Quantity")
  private int quantity = 1;
}
