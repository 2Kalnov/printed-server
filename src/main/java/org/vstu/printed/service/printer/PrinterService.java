package org.vstu.printed.service.printer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.PrinterDto;
import org.vstu.printed.persistence.printer.Printer;
import org.vstu.printed.repository.PrinterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrinterService {
  private final PrinterRepository printerRepository;

  public List<PrinterDto> getPrintersForSpot(int spotId) {
    return printerRepository.findBySpotIdNative(spotId).stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public PrinterDto getPrinterById(int id) {
    Optional<Printer> foundPrinter = printerRepository.findByIdNative(id);
    return foundPrinter.map(this::mapToDto).orElse(null);
  }

  public List<PrinterDto> getAllPrinters() {
    return printerRepository.findAllNative().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public boolean addPrinterForSpot(PrinterDto printerInfo, int spotId) {
    int recordsInserted = printerRepository.saveNative(printerInfo.getName(), printerInfo.getSize(), spotId, printerInfo.isColorful());
    if(recordsInserted > 0)
      return true;
    return false;
  }

  private PrinterDto mapToDto(Printer printer) {
    PrinterDto dto = new PrinterDto();

    dto.setName(printer.getName());
    dto.setColorful(printer.isColorful());
    dto.setSize(printer.getSize().toString());

    return dto;
  }
}
