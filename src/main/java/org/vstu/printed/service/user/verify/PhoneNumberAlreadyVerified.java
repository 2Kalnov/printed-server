package org.vstu.printed.service.user.verify;

public class PhoneNumberAlreadyVerified extends Exception {
  public PhoneNumberAlreadyVerified() {
    super("Заданный номер телефон уже проверен в системе");
  }

  public PhoneNumberAlreadyVerified(String message) {
    super(message);
  }
}
