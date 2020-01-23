package org.vstu.printed.service.spot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.SpotCreationDto;
import org.vstu.printed.dto.SpotDto;
import org.vstu.printed.dto.SpotUpdatingDataDto;
import org.vstu.printed.persistence.spot.Spot;
import org.vstu.printed.persistence.spotstatus.SpotStatus;
import org.vstu.printed.repository.SpotRepository;
import org.vstu.printed.repository.SpotStatusRepository;
import org.vstu.printed.service.spotstatus.SpotStatusService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {
  private final SpotRepository spotRepository;
  private final SpotStatusService spotStatusService;

  public boolean addSpot(SpotCreationDto spotData) {
    short statusId = spotStatusService.getStatusIdByName(spotData.getStatus());

    boolean successfulSave;
    int rowsInserted = spotRepository.saveNative(
            spotData.getAdminId(),
            spotData.getLocation()[0],
            spotData.getLocation()[1],
            spotData.getAddress(),
            statusId
    );

    successfulSave = rowsInserted == 1;

    return successfulSave;
  }

  public void updateSpot(SpotUpdatingDataDto patchData, int spotId) throws Exception {
    Optional<Spot> foundSpot = spotRepository.findById(spotId);
    if(foundSpot.isPresent()) {
      String newAddress = patchData.getAddress();
      String newStatus = patchData.getStatus();
      Double[] newLocation = patchData.getLocation();

      if(newAddress != null && newLocation != null)
        spotRepository.updateLocation(newLocation[0], newLocation[1], newAddress, spotId);
      else if(newStatus != null)
        spotRepository.updateStatus(spotStatusService.getStatusIdByName(newStatus), spotId);
      else
        throw new Exception("You have provided incorrect spot updating data");
    }
    else
      throw new Exception("Can not update; did not find such spot");
  }

  public int getAdminIdForSpot(int spotId) throws SpotNotFoundException {
    Optional<Spot> foundSpot = spotRepository.findById(spotId);
    if(foundSpot.isPresent())
      return foundSpot.get().getAdmin().getId();
    else
      throw new SpotNotFoundException("Spot with given id is not found");
  }

  public SpotDto getSpot(int spotId) throws SpotNotFoundException {
    Optional<Spot> foundSpot = spotRepository.findById(spotId);
    if(foundSpot.isPresent())
      return foundSpot.map(this::mapToDto).get();
    else
      throw new SpotNotFoundException("Spot with given id is not found");
  }

  public List<SpotDto> getAdminSpots(int adminId) {
    SpotDto spot;
    List<Spot> foundSpots = spotRepository.findByAdminIdNative(adminId);
    return foundSpots.stream().map(this::mapToDto).collect(Collectors.toList());
  }

  private SpotDto mapToDto(Spot spot) {
    SpotDto spotDto = new SpotDto();

    spotDto.setId(spot.getId());
    spotDto.setAddress(spot.getAddress());
    spotDto.setStatus("active");
    spotDto.setName(spot.getName());

    return spotDto;
  }
}
