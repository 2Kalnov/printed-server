package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vstu.printed.persistence.orderstatus.OrderStatus;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Short> {
  Optional<OrderStatus> findByStatus(String statusName);
}
