package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.persistence.spot.Spot;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Integer> {
  @Query(value = "select Id, [Name], AdminId, [Location], [Address], [StatusId] from Spots where AdminId = :adminId", nativeQuery = true)
  List<Spot> findByAdminIdNative(@Param("adminId") int adminId);

  String saveNativeQuery = "insert Spots values(null, :adminId, geography\\:\\:Point(:lat, :long, 4326), :address, :statusId)";

  @Modifying
  @Transactional
  @Query(value = saveNativeQuery, nativeQuery = true)
  int saveNative(
          @Param("adminId") int adminId,
          @Param("lat") double latitude,
          @Param("long") double longitude,
          @Param("address") String address,
          @Param("statusId") short statusId);

  String updateLocationQuery = "update Spots " +
          "set [Location] = Geography\\:\\:Point(:lat, :long, 4326), " +
          "[Address] = :address " +
          "where Spots.Id = :spotId";
  @Modifying
  @Transactional
  @Query(value = updateLocationQuery, nativeQuery = true)
  int updateLocation(
          @Param("lat") double latitude,
          @Param("long") double longitude,
          @Param("address") String address,
          @Param("spotId") int spotId
  );

  String updateStatusQuery = "update Spots " +
          "set StatusId = :statusId " +
          "where Id = :spotId";

  @Modifying
  @Transactional
  @Query(value = updateStatusQuery, nativeQuery = true)
  int updateStatus(
          @Param("statusId") short statusId,
          @Param("spotId") int spotId
  );

  @Modifying
  @Transactional
  void deleteById(int spotId);
}
