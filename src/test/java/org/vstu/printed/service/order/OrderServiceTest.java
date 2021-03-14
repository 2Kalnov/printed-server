package org.vstu.printed.service.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vstu.printed.dto.DocumentDto;
import org.vstu.printed.dto.OrderDataDto;
import org.vstu.printed.persistence.order.Order;
import org.vstu.printed.persistence.orderstatus.OrderStatus;
import org.vstu.printed.repository.OrderRepository;
import org.vstu.printed.service.document.DocumentService;
import org.vstu.printed.service.orderstatus.OrderStatusService;
import org.vstu.printed.service.receiveoption.ReceiveOptionService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

class OrderServiceTest {

  private OrderRepository orderRepository;
  private ReceiveOptionService receiveOptionService;
  private OrderStatusService orderStatusService;
  private DocumentService documentService;

  private OrderService orderService;

  private Order mockedEmptyOrder;
  private DocumentDto mockedOrderDocument;

  private final int MOCKED_DOCUMENT_ID = 1;
  private final String MOCKED_DOCUMENT_NAME = "java_se_8.pdf";
  private final int MOCKED_DOCUMENT_PAGES_COUNT = 5;
  private final int CLIENT_ID = 1;
  private final int ORDER_ID = 1;
  private final String PLACED_ORDER_STATUS_NAME = "placed";
  private final String RECEIVE_OPTION = "personal";
  private final OrderStatus PLACED_ORDER_STATUS = new OrderStatus((short)1);
  private final Timestamp CREATION_TIME = new Timestamp(new Date().getTime());

  @BeforeEach
  void setUpConstants() {
    PLACED_ORDER_STATUS.setStatus(PLACED_ORDER_STATUS_NAME);
  }

  @BeforeEach
  void setUp() {
    orderRepository = Mockito.mock(OrderRepository.class);
    receiveOptionService = Mockito.mock(ReceiveOptionService.class);
    orderStatusService = Mockito.mock(OrderStatusService.class);
    documentService = Mockito.mock(DocumentService.class);

    orderService = new OrderService(orderRepository, receiveOptionService, orderStatusService, documentService);

    mockedEmptyOrder = new Order(ORDER_ID, new byte[]{});
    mockedEmptyOrder.setClientId(CLIENT_ID);
    mockedEmptyOrder.setStatus(PLACED_ORDER_STATUS);
    mockedEmptyOrder.setCreatedAt(CREATION_TIME);

    mockedOrderDocument = new DocumentDto(
            MOCKED_DOCUMENT_ID, 0, MOCKED_DOCUMENT_NAME, CLIENT_ID, MOCKED_DOCUMENT_PAGES_COUNT);
  }

  @Test
  @DisplayName("Размещение заказа клиентом")
  void placeOrderByClient() {
    mockOrderPlacement();

    final double[] clientLocation = {44.44, 44.44};
    final int radius = 1000;

    OrderDataDto placementData = new OrderDataDto(clientLocation, radius, RECEIVE_OPTION);
    BigDecimal expectedCost = new BigDecimal(mockedOrderDocument.getPagesCount() * 3.0);

    orderService.placeOrder(placementData, CLIENT_ID, ORDER_ID);

    Mockito.verify(orderRepository).placeOrderNative(expectedCost, clientLocation[0], clientLocation[1], radius, (short)2, ORDER_ID);
  }

  @Test
  @DisplayName("Размещение заказа, созданного другим пользователем")
  void placeOrderByInvalidClient() {
    mockOrderPlacement();

    OrderDataDto placementData = new OrderDataDto(new double[] {}, Mockito.anyInt(), RECEIVE_OPTION);

    // Placing order giving invalid client id
    Assertions.assertFalse(orderService.placeOrder(placementData, CLIENT_ID + 1, ORDER_ID));
  }

  private void mockOrderPlacement() {
    Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(mockedEmptyOrder));
    Mockito.when(receiveOptionService.getOptionIdByName("personal")).thenReturn((short)2);
    Mockito.when(documentService.getAllDocumentsFromOrder(ORDER_ID)).thenReturn(Arrays.asList(mockedOrderDocument));
  }
}