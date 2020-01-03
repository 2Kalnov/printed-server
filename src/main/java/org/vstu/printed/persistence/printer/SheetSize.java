package org.vstu.printed.persistence.printer;

public enum SheetSize {
  A4("A4"), A3("A4");

  private String sizeName;

  SheetSize(String sizeName) {
    this.sizeName = sizeName;
  }
}
