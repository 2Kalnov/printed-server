package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vstu.printed.persistence.orderstatus.OrderStatus;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Short> {
  Optional<OrderStatus> findByStatus(String statusName);

  @Query(value="select top 1 * from OrderStatuses where [Status] = :status", nativeQuery = true)
  Optional<OrderStatus> findByStatusNative(@Param("status") String statusName);
}
