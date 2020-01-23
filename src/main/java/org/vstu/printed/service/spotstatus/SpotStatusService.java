package org.vstu.printed.service.spotstatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.vstu.printed.persistence.spotstatus.SpotStatus;
import org.vstu.printed.repository.SpotStatusRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpotStatusService {
  private final SpotStatusRepository repository;

  public short getStatusIdByName(String statusName) {
    Optional<SpotStatus> spotStatus = repository.findByStatus(statusName);
    return spotStatus.map(SpotStatus::getId).orElse((short)0);
  }
}
