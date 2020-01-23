package org.vstu.printed.persistence.spotRating;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SpotRatings")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class SpotRating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(name = "Rating")
  private final double rating;

  @Column(name = "Review")
  private final String review;

  @Column(name = "SpotId")
  private final Integer spotId;

  @Column(name = "UserId")
  private final Integer userId;
}
