package org.vstu.printed.persistence.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.vstu.printed.persistence.orderstatus.OrderStatus;
import org.vstu.printed.persistence.receiveoption.ReceiveOption;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "Orders")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final int id;

  @Column(name="CreatedAt", nullable = false)
  private Timestamp createdAt;

  @Column(name = "DoneAt")
  private Timestamp doneAt;

  @Column(name = "ReceivedAt")
  private Timestamp receivedAt;

  @Column(name = "Cost", nullable = false)
  private BigDecimal cost = BigDecimal.ZERO;

  @Column(name = "Location")
  private final byte[] location;

  @Column(name = "Radius")
  private Integer radius = 50;

  @ManyToOne
  @JoinColumn(name = "ReceiveOptionId", referencedColumnName = "Id", nullable = false)
  private ReceiveOption receiveOption;

  @ManyToOne
  @JoinColumn(name = "StatusId", referencedColumnName = "Id")
  private OrderStatus status;

  @Column(name = "UserId")
  private Integer clientId;

  @Column(name = "SpotId")
  private Integer spotId = null;

  public void setCost(BigDecimal cost) {
    if(this.cost.equals(BigDecimal.ZERO))
      this.cost = cost;
  }

  public void setClientId(Integer clientId) {
    if(this.clientId == null)
      this.clientId = clientId;
  }

  public void setCreatedAt(Timestamp createdAt) {
    if(this.createdAt == null)
      this.createdAt = createdAt;
  }
}
