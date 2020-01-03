package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vstu.printed.persistence.spotRating.SpotRating;

import java.util.List;

public interface SpotRatingRepository extends JpaRepository<SpotRating, Integer> {
  List<SpotRating> findAllBySpotId(int spotId);
}
