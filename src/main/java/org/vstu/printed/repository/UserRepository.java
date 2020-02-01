package org.vstu.printed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.persistence.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  List<User> findAll();

  @Query(value = "select * from Users where Users.Id = :userId", nativeQuery = true)
  Optional<User> findByIdNative(@Param("userId") int id);

  @Query(value = "select * from Users where PhoneNumber = :phone", nativeQuery = true)
  User findByPhoneNumberNative(@Param("phone") String phoneNumber);

  User findByPhoneNumber(String phoneNumber);

  @Query(value = "select * from Users where Email = :email", nativeQuery = true)
  User findByEmail(@Param("email") String email);

  @Modifying
  @Transactional
  @Query(value = "insert Users values (:roleId, :email, :password, :phone, :accountId, :name)", nativeQuery = true)
  void insert(
          @Param("email") String email,
          @Param("password") String password,
          @Param("phone") String phoneNumber,
          @Param("accountId") int accountNumber,
          @Param("roleId") short role,
          @Param("name") String name);

  @Modifying
  @Transactional
  @Query(value = "update Users set PhoneNumber = :phoneNumber where Id = :userId", nativeQuery = true)
  void updatePhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("userId") int userId);

  @Modifying
  @Transactional
  @Query(value = "update Users set Email = :email where Id = :userId", nativeQuery = true)
  void updateEmail(@Param("email") String email, @Param("userId") int userId);

  @Modifying
  @Transactional
  @Query(value = "update Users set Email = :email, PhoneNumber = :phoneNumber where Id = :userId", nativeQuery = true)
  void updateEmailAndPhoneNumber(
          @Param("phoneNumber") String phoneNumber,
          @Param("email") String email,
          @Param("userId") int userId
  );
}
