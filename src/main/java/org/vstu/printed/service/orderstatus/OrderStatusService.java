package org.vstu.printed.service.orderstatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.persistence.orderstatus.OrderStatus;
import org.vstu.printed.repository.OrderStatusRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
  private final OrderStatusRepository repository;

  public Short getStatusIdByName(String statusName) {
    System.out.println("Status in 'getStatusIdByName': " + statusName);

    Optional<OrderStatus> orderStatus = repository.findByStatusNative(statusName);
    return orderStatus.map(OrderStatus::getId).orElse(null);
  }

  public OrderStatus getStatusByName(String statusName) {
    Optional<OrderStatus> orderStatus = repository.findByStatus(statusName);
    return orderStatus.orElse(null);
  }
}
