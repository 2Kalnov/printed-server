package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vstu.printed.persistence.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  @Query(value = "select Orders.* from Orders join Users on Orders.UserId = Users.Id where UserId = ?1 and Orders.Id = ?2;", nativeQuery = true)
  Optional<Order> findUserOrder(int userId, int orderId);

  @Query(value = "select Orders.* from Orders join Users on Orders.UserId = Users.Id where UserId = ?1", nativeQuery = true)
  List<Order> findUserOrders(int userId);
}
