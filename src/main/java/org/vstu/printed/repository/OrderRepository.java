package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.persistence.order.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  @Query(value = "select Orders.* from Orders join Users on Orders.UserId = Users.Id where UserId = ?1 and Orders.Id = ?2;", nativeQuery = true)
  Optional<Order> findUserOrder(int userId, int orderId);

  @Query(value = "select Orders.* from Orders join Users on Orders.UserId = Users.Id where UserId = ?1", nativeQuery = true)
  List<Order> findUserOrders(int userId);

  String saveNativeQuery = "insert Orders values(:cost, :createdAt, null, null, Geography\\:\\:Point(:lat, :long, 4326), :radius, :optionId, 1, null, :userId)";
  @Query(value = saveNativeQuery, nativeQuery = true)
  @Modifying
  @Transactional
  int saveNative(
          @Param("cost") BigDecimal cost,
          @Param("createdAt") Timestamp createdAt,
          @Param("lat") double latitude,
          @Param("long") double longitude,
          @Param("radius") int radius,
          @Param("userId") int userId,
          @Param("optionId") short receiveOptionId
  );

  @Query(value = "update Orders set StatusId = :statusId, ReceivedAt = :receivedAt or :receivedAt is null, DoneAt = :doneAt or :doneAt is null, SpotId = :spotId, Radius = :radius where Id = :orderId", nativeQuery = true)
  @Modifying
  @Transactional
  int updateNative(
          @Param("doneAt") Timestamp doneAt,
          @Param("receivedAt") Timestamp receivedAt,
          @Param("statusId") Short statusId,
          @Param("spotId") Integer spotId,
          @Param("radius") Integer radius,
          @Param("orderId") int orderId
  );

  @Query(value = "select Id, Cost, CreatedAt, DoneAt, ReceivedAt, [Location], Radius, ReceiveOptionId, StatusId, SpotId, UserId from Orders where Id = :orderId", nativeQuery = true)
  Optional<Order> findByIdNative(
          @Param("orderId") int orderId
  );

}
