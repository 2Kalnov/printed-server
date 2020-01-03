package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vstu.printed.persistence.printer.Printer;
import org.vstu.printed.persistence.printer.SheetSize;

import java.util.List;
import java.util.Optional;

public interface PrinterRepository extends JpaRepository<Printer, Integer> {
  @Query(value = "select Id, [Name], Size, SpotId, Colorful from Printers where Id = :id", nativeQuery = true)
  Optional<Printer> findByIdNative(@Param("id") int printerId);

  @Query(value = "select Id, [Name], Size, SpotId, Colorful from Printers where SpotId = :spotId", nativeQuery = true)
  List<Printer> findBySpotIdNative(@Param("spotId") int spotId);

  @Query(value = "select Id, [Name], Size, SpotId, Colorful from Printers", nativeQuery = true)
  List<Printer> findAllNative();

  @Query(value = "insert Printers values(:name, :size, :spotId, :colorful)", nativeQuery = true)
  int saveNative(
          @Param("name") String name,
          @Param("size") String size,
          @Param("spotId") int spotId,
          @Param("colorful") boolean isColorful
  );
}
