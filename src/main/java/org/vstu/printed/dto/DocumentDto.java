package org.vstu.printed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DocumentDto {
  private int id;
  private int size;
  private String name;
  private int userId;
  private int pagesCount;

}
