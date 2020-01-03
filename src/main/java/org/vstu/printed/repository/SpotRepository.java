package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.persistence.spot.Spot;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Integer> {
  @Query(value = "select Id, [Name], AdminId, [Location], [Address], [StatusId] from Spots where AdminId = :adminId", nativeQuery = true)
  Optional<Spot> findByAdminIdNative(@Param("adminId") int adminId);

  @Modifying
  @Transactional
  @Query(value = "insert Spots values(null, :adminId, geography::Point(:lat, :long, 4326), :address, :statusId)", nativeQuery = true)
  int saveNative(
          @Param("adminId") int adminId,
          @Param("lat") double latitude,
          @Param("long") double longitude,
          @Param("address") String address,
          @Param("statusId") short statusId);
}
