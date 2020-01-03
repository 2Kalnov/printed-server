package org.vstu.printed.persistence.printer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Printers")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class Printer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Name")
  private final String name;

  @Column(name = "SpotId")
  private final int spotId;

  @Column(name = "Colorful")
  private final boolean isColorful;

  @Column(name = "Size")
  private final SheetSize size;
}