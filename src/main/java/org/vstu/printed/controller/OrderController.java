package org.vstu.printed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.*;
import org.vstu.printed.security.jwt.JwtUser;
import org.vstu.printed.service.document.DocumentService;
import org.vstu.printed.service.order.IllegalOrderPickUpException;
import org.vstu.printed.service.order.OrderService;
import org.vstu.printed.service.spot.SpotNotFoundException;
import org.vstu.printed.service.spot.SpotService;

import java.util.List;

@RestController
public class OrderController {
  private final OrderService orderService;
  private final SpotService spotService;
  private final DocumentService documentService;

  private int getUserIdFromAuthToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser userInfo = (JwtUser)authentication.getPrincipal();
    return userInfo.getId();
  }

  @Autowired
  public OrderController(OrderService orderService, DocumentService documentService, SpotService spotService) {
    this.orderService = orderService;
    this.documentService = documentService;
    this.spotService = spotService;
  }

  @GetMapping("/users/{userId}/orders")
  public ResponseEntity<List<OrderDto>> userOrders(@PathVariable int userId, @RequestParam("status") String orderStatus) {
    List<OrderDto> orders = orderService.getUserOrders(userId, orderStatus);
    if(!orders.isEmpty())
      return ResponseEntity.ok(orders);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping("/users/{userId}/orders/{orderId}")
  public ResponseEntity<OrderDto> userOrders(@PathVariable int userId, @PathVariable int orderId) {
    OrderDto order = orderService.getUserOrder(userId, orderId);
    if(order != null)
      return ResponseEntity.ok(order);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping("/orders/{id}/documents")
  public ResponseEntity<List<DocumentDto>> orderDocuments(@PathVariable int id) {
    List<DocumentDto> orderDocuments = documentService.getAllDocumentsFromOrder(id);
    if(!orderDocuments.isEmpty())
      return ResponseEntity.ok(orderDocuments);
    else
      return ResponseEntity.notFound().build();
  }

  @PutMapping("/orders/{orderId}")
  public ResponseEntity placeOrder(@PathVariable int orderId, @RequestBody OrderDataDto orderData) {
    int userId = getUserIdFromAuthToken();
    boolean successfulSave = orderService.placeOrder(orderData, userId, orderId);
    if(successfulSave)
      return ResponseEntity.status(HttpStatus.CREATED).build();
    else
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @PostMapping("/orders/new")
  public ResponseEntity<Integer> createOrder() {
    int userId = getUserIdFromAuthToken();
    int newOrderId = orderService.createEmptyOrder(userId);

    return ResponseEntity.ok(newOrderId);
  }

  @PatchMapping("/orders/{orderId}")
  public ResponseEntity updateOrder(@PathVariable int orderId, @RequestBody OrderUpdatingDataDto patchData) {
    try {
      orderService.updateOrder(patchData, orderId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping("/orders/placed")
  public ResponseEntity<List<OrderDto>> getAllPlacedOrders() {
    List<OrderDto> placedOrders = orderService.allPlacedOrders();
    if(!placedOrders.isEmpty())
      return ResponseEntity.ok(placedOrders);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping("/spots/{spotId}/orders")
  public ResponseEntity<List<OrderForManagerDto>> getSpotOrders(@PathVariable int spotId, @RequestParam("status") String orderStatus) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser userInfo = (JwtUser)authentication.getPrincipal();
    int userId = userInfo.getId();

    try {
      int spotAdminId = spotService.getAdminIdForSpot(spotId);
      if(spotAdminId == userId)
        return ResponseEntity.ok(orderService.getOrdersForSpot(spotId, orderStatus));
      else
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    } catch(SpotNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/orders")
  public ResponseEntity<List<OrderForManagerDto>> getAllOrders() {
    List<OrderForManagerDto> orders = orderService.getAllOrders();
    if(!orders.isEmpty())
      return ResponseEntity.ok(orders);
    else
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/orders/{orderId}")
  public ResponseEntity deleteOrder(@PathVariable int orderId) {
    try {
      orderService.deleteOrder(orderId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch(Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
