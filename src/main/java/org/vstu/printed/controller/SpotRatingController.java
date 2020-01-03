package org.vstu.printed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vstu.printed.dto.SpotRatingDto;
import org.vstu.printed.service.spotrating.SpotRatingService;

import java.util.List;

@RestController
@RequestMapping("/spots/{spotId}/ratings")
@RequiredArgsConstructor
public class SpotRatingController {
  private final SpotRatingService ratingService;

  @GetMapping("/{id}")
  public ResponseEntity<SpotRatingDto> getSpotRating(@PathVariable int id) {
    SpotRatingDto ratingDto = ratingService.getRating(id);
    if(ratingDto != null)
      return ResponseEntity.ok(ratingDto);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping
  public ResponseEntity<List<SpotRatingDto>> getSpotRatings(@PathVariable int spotId) {
    List<SpotRatingDto> ratingsDtos = ratingService.getRatingsForSpot(spotId);
    if(!ratingsDtos.isEmpty())
      return ResponseEntity.ok(ratingsDtos);
    else
      return ResponseEntity.notFound().build();
  }
}
