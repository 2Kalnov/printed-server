package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.SpotCreationDto;
import org.vstu.printed.dto.SpotUpdatingDataDto;
import org.vstu.printed.security.jwt.JwtUser;
import org.vstu.printed.dto.SpotDto;
import org.vstu.printed.service.spot.SpotNotFoundException;
import org.vstu.printed.service.spot.SpotService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spots")
public class SpotController {
  private final SpotService spotService;

  private int getUserIdFromAuthToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser userInfo = (JwtUser)authentication.getPrincipal();
    return userInfo.getId();
  }

  @PatchMapping("/{spotId}")
  public ResponseEntity updateSpot(@PathVariable int spotId, @RequestBody SpotUpdatingDataDto spotData) {
    try {
      int spotAdminId = spotService.getAdminIdForSpot(spotId);
      int userId = getUserIdFromAuthToken();
      if(spotAdminId == userId) {
        spotService.updateSpot(spotData, spotId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @PostMapping
  public ResponseEntity addSpot(@RequestBody SpotCreationDto spotDto) {
    boolean spotWasCreated = spotService.addSpot(spotDto, getUserIdFromAuthToken());
    if(spotWasCreated)
      return ResponseEntity.status(HttpStatus.CREATED).build();
    else
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @GetMapping("/{spotId}")
  public ResponseEntity<SpotDto> getSpot(@PathVariable int spotId) {
    try {
      SpotDto dto = spotService.getSpot(spotId);
      return ResponseEntity.ok(dto);
    } catch(SpotNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping()
  public ResponseEntity<List<SpotDto>> adminSpots(@RequestParam("adminId") int adminId) {
    List<SpotDto> spots = spotService.getAdminSpots(adminId);
    return ResponseEntity.ok(spots);
  }

  @DeleteMapping("/{spotId}")
  public ResponseEntity deleteSpot(@PathVariable int spotId) {
    try {
      int userId = getUserIdFromAuthToken();
      int spotAdminId = spotService.getAdminIdForSpot(spotId);
      if(userId == spotAdminId) {
        spotService.deleteSpot(spotId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
      else
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch(Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
