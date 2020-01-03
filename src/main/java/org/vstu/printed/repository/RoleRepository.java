package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vstu.printed.persistence.role.Role;

public interface RoleRepository extends JpaRepository<Role, Short> {

  @Query(value="select * from Roles where [Name] = :name", nativeQuery = true)
  Role findByNameNative(@Param("name") String name);
}
