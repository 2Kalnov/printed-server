package org.vstu.printed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto {
  private int id;
  private int size;
  private String name;
  private int userId;
  private int pagesCount;
}
