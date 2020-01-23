package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vstu.printed.persistence.role.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Short> {
  @Query(value="select top 1 Id, [Name] from Roles where [Name] = :roleName", nativeQuery = true)
  Optional<Role> findByNameNative(@Param("roleName") String name);
}
