package org.vstu.printed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.OrderDataDto;
import org.vstu.printed.dto.OrderUpdatingDataDto;
import org.vstu.printed.persistence.order.Order;
import org.vstu.printed.dto.OrderDto;
import org.vstu.printed.dto.DocumentDto;
import org.vstu.printed.service.document.DocumentService;
import org.vstu.printed.service.order.OrderService;

import java.util.List;

@RestController
public class OrderController {
  private final OrderService orderService;
  private final DocumentService documentService;

  @Autowired
  public OrderController(OrderService orderService, DocumentService documentService) {
    this.orderService = orderService;
    this.documentService = documentService;
  }

  @GetMapping("users/{userId}/orders/")
  public ResponseEntity<List<OrderDto>> userOrders(@PathVariable int userId) {
    List<OrderDto> orders = orderService.getUserOrders(userId);
    if(!orders.isEmpty())
      return ResponseEntity.ok(orders);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping("users/{userId}/orders/{orderId}")
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

  @PostMapping("/orders/new")
  public ResponseEntity createOrder(@RequestBody OrderDataDto orderData) {
    boolean successfulSave = orderService.createNewOrder(orderData);
    if(successfulSave)
      return ResponseEntity.status(HttpStatus.CREATED).build();
    else
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
}
