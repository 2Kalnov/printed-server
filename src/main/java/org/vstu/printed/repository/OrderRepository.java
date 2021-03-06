package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.dto.OrderForManagerDto;
import org.vstu.printed.persistence.order.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  @Query(value = "select Orders.Id, Cost, CreatedAt, ReceivedAt, DoneAt, [Location], Radius, ReceiveOptionId, StatusId, SpotId, UserId " +
          "from Orders join Users on Orders.UserId = Users.Id where UserId = :userId and Orders.Id = :orderId", nativeQuery = true)
  Optional<Order> findUserOrder(
          @Param("userId") int userId,
          @Param("orderId") int orderId
  );

  @Query(value = "select Orders.Id, Cost, CreatedAt, ReceivedAt, DoneAt, [Location], Radius, ReceiveOptionId, StatusId, SpotId, UserId" +
          " from Orders join Users on Orders.UserId = Users.Id where UserId = :userId and StatusId = :statusId", nativeQuery = true)
  List<Order> findUserOrders(@Param("userId") int userId, @Param("statusId") short statusId);

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

  String placeNativeQuery = "update Orders " +
          "set Cost = :cost, " +
          "[Location] = Geography\\:\\:Point(:lat, :long, 4326)," +
          "Radius = :radius, " +
          "ReceiveOptionId = :optionId " +
          "where Orders.Id = :orderId";
  @Query(value = placeNativeQuery, nativeQuery = true)
  @Transactional
  @Modifying
  int placeOrderNative(
          @Param("cost") BigDecimal cost,
          @Param("lat") double latitude,
          @Param("long") double longitude,
          @Param("radius") int radius,
          @Param("optionId") short receiveOptionId,
          @Param("orderId") int orderId
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

  String allPlacedOrdersQuery = "select Id, Cost, CreatedAt, DoneAt, ReceivedAt, [Location], Radius, ReceiveOptionId, StatusId, SpotId, UserId from Orders where StatusId = 1";
  @Query(value = allPlacedOrdersQuery, nativeQuery = true)
  List<Order> findAllPlaced();

  @Modifying
  @Transactional
  void deleteById(int orderId);

  @Modifying
  @Transactional
  @Query(value = "update Orders set SpotId = null where SpotId = :spotId and StatusId = 3", nativeQuery = true)
  void unsetSpotForOrdersInWork(@Param("spotId") int spotId);

  @Query(value = "select Id, Cost, CreatedAt, DoneAt, ReceivedAt, [Location], Radius, ReceiveOptionId, StatusId, SpotId, UserId " +
          "from Orders where SpotId = :spotId and StatusId = :statusId", nativeQuery = true)
  List<Order> findBySpotIdAndStatusNative(
          @Param("spotId") int spotId,
          @Param("statusId") int statusId
  );

  List<Order> findAll();
}
