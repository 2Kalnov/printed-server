package org.vstu.printed.persistence.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "Orders")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name="CreatedAt")
  private final Timestamp createdAt;

  @Column(name = "DoneAt")
  @Nullable
  private Timestamp doneAt;

  @Column(name = "ReceivedAt")
  @Nullable
  private Timestamp receivedAt;

  @Column(name = "Cost")
  private final BigDecimal cost;

  @Column(name = "Location")
  private final byte[] location;

  @Column(name = "Radius")
  private final int radius;

  @Column(name = "ReceiveOptionId")
  private final short receiveOptionId;

  @Column(name = "StatusId")
  private short statusId;

  @Column(name = "UserId")
  private final int clientId;

  @Column(name = "SpotId")
  private final int spotId;

}
