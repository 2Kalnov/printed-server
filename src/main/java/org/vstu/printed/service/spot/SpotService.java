package org.vstu.printed.service.spot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.SpotCreationDto;
import org.vstu.printed.dto.SpotDto;
import org.vstu.printed.persistence.spot.Spot;
import org.vstu.printed.repository.SpotRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpotService {
  private final SpotRepository repository;

  public boolean addSpot(SpotCreationDto spotData) {
    boolean successfulSave;
    int rowsInserted = repository.saveNative(
            spotData.getAdminId(),
            spotData.getLocation()[0],
            spotData.getLocation()[1],
            spotData.getAddress(),
            spotData.getStatusId()
    );

    successfulSave = rowsInserted == 1;

    return successfulSave;
  }

  public int getAdminIdForSpot(int spotId) throws SpotNotFoundException {
    Optional<Spot> foundSpot = repository.findById(spotId);
    if(foundSpot.isPresent())
      return foundSpot.get().getAdmin().getId();
    else
      throw new SpotNotFoundException("Spot with given id is not found");
  }

  public SpotDto getSpot(int spotId) throws SpotNotFoundException {
    Optional<Spot> foundSpot = repository.findById(spotId);
    if(foundSpot.isPresent())
      return foundSpot.map(this::mapToDto).get();
    else
      throw new SpotNotFoundException("Spot with given id is not found");
  }

  public SpotDto getAdminSpot(int adminId) {
    SpotDto spot;
    Optional<Spot> foundSpot = repository.findByAdminIdNative(adminId);
    if(foundSpot.isPresent())
      spot = mapToDto(foundSpot.get());
    else
      spot = null;

    return spot;
  }

  private SpotDto mapToDto(Spot spot) {
    SpotDto spotDto = new SpotDto();

    spotDto.setId(spot.getId());
    spotDto.setAddress(spot.getAddress());
    spotDto.setStatusId((short)1);

    return spotDto;
  }
}
