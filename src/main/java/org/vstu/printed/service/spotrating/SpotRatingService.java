package org.vstu.printed.service.spotrating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.dto.SpotRatingDto;
import org.vstu.printed.persistence.spotRating.SpotRating;
import org.vstu.printed.repository.SpotRatingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotRatingService {
  private final SpotRatingRepository ratingRepository;

  public void addRating(SpotRatingDto ratingData, int userId, int spotId) {
    ratingRepository.save(mapFromDto(ratingData, spotId, userId));
  }

  public SpotRatingDto getRating(int ratingId) {
    Optional<SpotRating> foundRating = ratingRepository.findById(ratingId);
    return foundRating.map(this::mapToDto).orElse(null);
  }

  public List<SpotRatingDto> getRatingsForSpot(int spotId) {
    return ratingRepository.findAllBySpotId(spotId).stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public List<SpotRatingDto> getAllRatings() {
    return ratingRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  private SpotRatingDto mapToDto(SpotRating spotRating) {
    SpotRatingDto dto = new SpotRatingDto();
    dto.setRating(spotRating.getRating());
    dto.setReview(spotRating.getReview());

    return dto;
  }

  private SpotRating mapFromDto(SpotRatingDto dto, int spotId, int userId) {
    SpotRating spotRating = new SpotRating(
            dto.getRating(),
            dto.getReview(),
            spotId,
            userId
    );

    return spotRating;
  }
}


