package org.vstu.printed.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.vstu.printed.persistence.order.Order;
import org.vstu.printed.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository repository;

  public ResponseEntity<List<Order>> getUserOrders(int userId) {
    List<Order> userOrders = repository.findUserOrders(userId);
    if(userOrders.size() != 0)
      return ResponseEntity.ok(userOrders);
    else
      return ResponseEntity.notFound().build();
  }

  public ResponseEntity<Order> getUserOrder(int userId, int orderId) {
    Optional<Order> orderData = repository.findUserOrder(userId, orderId);
    if(orderData.isPresent())
      return ResponseEntity.ok(orderData.get());
    else
      return ResponseEntity.notFound().build();
  }
}
