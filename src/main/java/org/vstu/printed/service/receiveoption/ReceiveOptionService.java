package org.vstu.printed.service.receiveoption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.persistence.receiveoption.ReceiveOption;
import org.vstu.printed.repository.ReceiveOptionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReceiveOptionService {
  private final ReceiveOptionRepository repository;

  public short getOptionIdByName(String optionName) {
    Optional<ReceiveOption> receiveOption = repository.findByOptionNative(optionName);
    return receiveOption.map(ReceiveOption::getId).orElse((short)0);
  }

  public ReceiveOption getOptionByName(String optionName) {
    Optional<ReceiveOption> option = repository.findByOption(optionName);
    return option.orElse(null);
  }
}
