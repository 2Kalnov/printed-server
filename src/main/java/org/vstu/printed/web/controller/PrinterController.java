package org.vstu.printed.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.PrinterDto;
import org.vstu.printed.service.printer.PrinterService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PrinterController {
  private final PrinterService printerService;

  @GetMapping("/printers")
  public ResponseEntity<List<PrinterDto>> getPrintersForSpot(@RequestParam("spotId") int spotId) {
    List<PrinterDto> printers = printerService.getPrintersForSpot(spotId);
    if(!printers.isEmpty())
      return ResponseEntity.ok(printers);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping("/printers/{id}")
  public ResponseEntity<PrinterDto> getPrinter(@PathVariable int id) {
    PrinterDto printer = printerService.getPrinterById(id);
    if(printer != null)
      return ResponseEntity.ok(printer);
    else
      return ResponseEntity.notFound().build();
  }

  @PostMapping("/spots/{spotId}/printers")
  public ResponseEntity addPrinterToSpot(@RequestBody PrinterDto printerInfo, @PathVariable int spotId) {
    boolean isAdded = printerService.addPrinterForSpot(printerInfo, spotId);
    if(isAdded)
      return ResponseEntity.status(HttpStatus.CREATED).build();
    else
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }
}
