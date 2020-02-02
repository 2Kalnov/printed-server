package org.vstu.printed.service.order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.*;
import org.vstu.printed.persistence.document.Document;
import org.vstu.printed.persistence.order.Order;
import org.vstu.printed.persistence.orderstatus.OrderStatus;
import org.vstu.printed.repository.OrderRepository;
import org.vstu.printed.repository.ReceiveOptionRepository;
import org.vstu.printed.service.document.DocumentService;
import org.vstu.printed.service.orderstatus.OrderStatusService;
import org.vstu.printed.service.receiveoption.ReceiveOptionService;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository repository;
  private final ReceiveOptionService receiveOptionService;
  private final OrderStatusService orderStatusService;
  private final DocumentService documentService;

  private final static double PAGE_COST = 3.0;

  public void unsetDeletedSpotForOrders(int spotId) {
    repository.unsetSpotForOrdersInWork(spotId);
  }

  public void deleteOrder(int orderId) {
    repository.deleteById(orderId);
  }

  public List<OrderDto> allPlacedOrders() {
    return repository.findAllPlaced().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public int createEmptyOrder(int clientId) {
    Order newOrder = new Order();
    OrderStatus status = orderStatusService.getStatusByName("placed");

    newOrder.setClientId(clientId);
    newOrder.setStatus(status);
    newOrder.setCreatedAt(new Timestamp(new Date().getTime()));

    return repository.save(newOrder).getId();
  }

  public boolean placeOrder(OrderDataDto orderData, int clientId, int orderId) {
    short orderReceiveOptionId = receiveOptionService.getOptionIdByName(orderData.getReceiveOption());
    double orderCost = 0.0;

    List<DocumentDto> orderDocuments = documentService.getAllDocumentsFromOrder(orderId);
    for(DocumentDto document : orderDocuments) {
      orderCost += document.getPagesCount() * PAGE_COST;
    }

    BigDecimal cost = new BigDecimal(orderCost);

    Optional<Order> order = repository.findById(orderId);

    int rowsInserted = 0;
    if(order.isPresent() && order.get().getClientId() == clientId) {
      rowsInserted = repository.placeOrderNative(
              cost,
              orderData.getLocation()[0],
              orderData.getLocation()[1],
              orderData.getRadius(),
              orderReceiveOptionId,
              orderId
      );
    }

    return rowsInserted == 1;
  }

  public void updateOrder(OrderUpdatingDataDto patchData, int orderId) throws Exception, IllegalOrderPickUpException {
    Optional<Order> foundOrder = repository.findByIdNative(orderId);
    if(foundOrder.isPresent()) {
      Order order = foundOrder.get();

      Integer newSpotId = patchData.getSpotId();
      Integer newRadius = patchData.getRadius();
      OrderStatus newStatus = orderStatusService.getStatusByName(patchData.getStatus());

      if(order.getSpotId() != null && newSpotId != null)
        throw new IllegalOrderPickUpException("Order is already picked up by another spot");

      if(newSpotId != null)
        order.setSpotId(newSpotId);
      if(newRadius != null)
        order.setRadius(newRadius);
      if(newStatus != null && !newStatus.equals("placed"))
        order.setStatus(newStatus);
      if(newStatus.getStatus().equals("received"))
        order.setReceivedAt(new Timestamp(new Date().getTime()));
      if(newStatus.getStatus().equals("ready"))
        order.setDoneAt(new Timestamp(new Date().getTime()));

      repository.save(order);
    }
    else
      throw new Exception("Can not update; did not find such order");
  }

  public List<OrderDto> getUserOrders(int userId) {
    List<Order> userOrders = repository.findUserOrders(userId);
    return userOrders.stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public OrderDto getUserOrder(int userId, int orderId) {
    Optional<Order> orderData = repository.findUserOrder(userId, orderId);
    return orderData.map(this::mapToDto).orElse(null);
  }

  public List<OrderForManagerDto> getOrdersForSpot(int spotId, String orderStatus) {
    short ordersStatusId = orderStatusService.getStatusIdByName(orderStatus);
    return repository.findBySpotIdAndStatusNative(spotId, ordersStatusId)
              .stream().map(this::mapToManagerDto).collect(Collectors.toList());
  }

  public List<OrderForManagerDto> getAllOrders() {
    return repository.findAll().stream().map(this::mapToManagerDto).collect(Collectors.toList());
  }

  private OrderDto mapToDto(Order order) {
    OrderDto orderDto = new OrderDto();

    Date receivedAt = order.getCreatedAt();
    Date doneAt = order.getDoneAt();
    Integer spotId = order.getSpotId();

    if(receivedAt != null)
      orderDto.setReceivedAt(receivedAt);
    if(doneAt != null)
      orderDto.setDoneAt(doneAt);
    if(spotId != null)
      orderDto.setSpotId(spotId);

    orderDto.setId(order.getId());
    orderDto.setCost(order.getCost().doubleValue());
    orderDto.setCreatedAt(order.getCreatedAt());
    orderDto.setReceiveOption(order.getReceiveOption().getOption());
    orderDto.setStatus(order.getStatus().getStatus());

    return orderDto;
  }

  private OrderForManagerDto mapToManagerDto(Order order) {
    OrderForManagerDto orderDto = new OrderForManagerDto();

    Date receivedAt = order.getCreatedAt();
    Date doneAt = order.getDoneAt();
    Integer spotId = order.getSpotId();

    if(receivedAt != null)
      orderDto.setReceivedAt(receivedAt);
    if(doneAt != null)
      orderDto.setDoneAt(doneAt);
    if(spotId != null)
      orderDto.setClientId(order.getClientId());

    orderDto.setId(order.getId());
    orderDto.setCost(order.getCost().doubleValue());
    orderDto.setCreatedAt(order.getCreatedAt());
    orderDto.setReceiveOption(order.getReceiveOption().getOption());
    orderDto.setStatus(order.getStatus().getStatus());
    orderDto.setSpotId(order.getSpotId());

    return orderDto;
  }
}
