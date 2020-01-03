package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.vstu.printed.dto.SpotAdminDto;
import org.vstu.printed.dto.SpotCreationDto;
import org.vstu.printed.security.jwt.JwtUser;
import org.vstu.printed.dto.SpotDto;
import org.vstu.printed.service.spot.SpotNotFoundException;
import org.vstu.printed.service.spot.SpotService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spots")
public class SpotController {
  private final SpotService spotService;

  @PostMapping
  public ResponseEntity addSpot(@RequestBody SpotCreationDto spotDto) {
    boolean spotWasCreated = spotService.addSpot(spotDto);
    if(spotWasCreated)
      return ResponseEntity.status(HttpStatus.CREATED).build();
    else
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @GetMapping("/{spotId}")
  public ResponseEntity<SpotDto> getSpot(@PathVariable int spotId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser userInfo = (JwtUser)authentication.getPrincipal();
    int userId = userInfo.getId();

    try {
      /*int adminId = spotService.getAdminIdForSpot(spotId);
      if(userId == adminId) {
        SpotDto dto = spotService.getSpot(spotId);
        return ResponseEntity.ok(dto);
      }
      else
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();*/
      SpotDto dto = spotService.getSpot(spotId);
      return ResponseEntity.ok(dto);
    } catch(SpotNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<SpotDto> adminSpot(@RequestBody SpotAdminDto spotAdminDto) {
    int userId = spotAdminDto.getUserId();
    SpotDto spot = spotService.getAdminSpot(userId);
    if(spot != null)
      return ResponseEntity.ok(spot);
    else
      return ResponseEntity.notFound().build();
  }
}
