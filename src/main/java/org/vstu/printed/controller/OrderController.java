package org.vstu.printed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.OrderDataDto;
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
  public ResponseEntity<List<Order>> userOrders(@PathVariable int userId) {
    return orderService.getUserOrders(userId);
  }

  @GetMapping("users/{userId}/orders/{orderId}")
  public ResponseEntity<Order> userOrders(@PathVariable int userId, @PathVariable int orderId) {
    return orderService.getUserOrder(userId, orderId);
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
  public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDataDto orderData) {
    return ResponseEntity.badRequest().build();
  }
}
