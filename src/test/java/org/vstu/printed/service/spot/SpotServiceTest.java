package org.vstu.printed.service.spot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.vstu.printed.dto.SpotCreationDto;
import org.vstu.printed.dto.SpotDto;
import org.vstu.printed.persistence.spot.Spot;
import org.vstu.printed.persistence.spotstatus.SpotStatus;
import org.vstu.printed.repository.SpotRepository;
import org.vstu.printed.service.order.OrderService;
import org.vstu.printed.service.spotstatus.SpotStatusService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpotServiceTest {
  private SpotService spotService;

  private SpotRepository spotRepository;
  private SpotStatusService spotStatusService;
  private OrderService orderService;

  private final int ADMIN_ID = 1;
  private final int SPOT_ID = 5;
  private final double[] LOCATION = {48.714356, 44.497994};
  private final String ADDRESS = "Волгоград, ул. Рокоссовского, 1";
  private SpotStatus inactiveStatus;
  private List<Spot> adminSpots;
  private Spot adminSpot;

  @BeforeEach
  void setUp() {
    spotRepository = Mockito.mock(SpotRepository.class);
    spotStatusService = Mockito.mock(SpotStatusService.class);
    orderService = Mockito.mock(OrderService.class);

    spotService = new SpotService(spotRepository, spotStatusService, orderService);

    inactiveStatus = new SpotStatus((short)1);
    inactiveStatus.setStatus("inactive");

    adminSpot = new Spot(SPOT_ID);
    adminSpot.setAddress(ADDRESS);
    adminSpot.setStatus(inactiveStatus);

    adminSpots = new ArrayList<>();
    adminSpots.add(adminSpot);
  }

  @Test
  @DisplayName("Удаление точки печати")
  void deleteSpot() throws SpotNotFoundException, SQLException {
    Mockito.when(spotRepository.findById(SPOT_ID)).thenReturn(Optional.of(adminSpot));
    spotService.deleteSpot(SPOT_ID);
    Mockito.when(spotRepository.findById(SPOT_ID)).thenReturn(Optional.empty());

    Assertions.assertThrows(SpotNotFoundException.class, () -> {
      spotService.getSpot(SPOT_ID);
    });
  }

  @Test
  @DisplayName("Создание новой точки печати")
  void addSpot() {
    Mockito.when(spotStatusService.getStatusIdByName("inactive")).thenReturn((short)1);
    Mockito.when(spotRepository.saveNative(ADMIN_ID, LOCATION[0], LOCATION[1], ADDRESS, (short)1)).thenReturn(1);
    Mockito.when(spotRepository.findByAdminIdNative(ADMIN_ID)).thenReturn(adminSpots);

    SpotCreationDto spotCreationDto = new SpotCreationDto(ADDRESS, LOCATION, "inactive");

    spotService.addSpot(spotCreationDto, ADMIN_ID);
    List<SpotDto> adminSpots = spotService.getAdminSpots(ADMIN_ID);

    Assertions.assertTrue(adminSpots.size() == 1 && adminSpots.get(0).getAddress().equals(ADDRESS));
  }
}