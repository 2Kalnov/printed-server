package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vstu.printed.persistence.spotstatus.SpotStatus;

import java.util.List;
import java.util.Optional;

public interface SpotStatusRepository extends JpaRepository<SpotStatus, Short> {
  Optional<SpotStatus> findByStatus(String statusName);

  List<SpotStatus> findAll();
}
