package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vstu.printed.persistence.receiveoption.ReceiveOption;

import java.util.Optional;

public interface ReceiveOptionRepository extends JpaRepository<ReceiveOption, Short> {
  @Query(value = "select Id, [Option] from ReceiveOptions where [Option] = :optionName", nativeQuery = true)
  Optional<ReceiveOption> findByOptionNative(String optionName);
}
