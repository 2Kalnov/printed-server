package org.vstu.printed.service.order;

public class IllegalOrderPickUpException extends Exception {
  public IllegalOrderPickUpException(String message) {
    super(message);
  }
}
