package org.vstu.printed.service.user;

public class DuplicateUserException extends Exception {
  public DuplicateUserException(String message) {
    super(message);
  }
}