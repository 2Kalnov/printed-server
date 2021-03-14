package org.vstu.printed.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vstu.printed.service.spotstatus.SpotStatusService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spotstatuses")
public class SpotStatusController {
  private final SpotStatusService statusService;

  @GetMapping()
  public ResponseEntity<List<String>> getAllStatuses() {
    List<String> spotStatuses = statusService.getAllStatuses();
    return ResponseEntity.ok(spotStatuses);
  }
}
