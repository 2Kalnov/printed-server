package org.vstu.printed.service.user.verify;

public class InvalidCodeException extends Exception {
  public InvalidCodeException(String message) {
    super(message);
  }
}
