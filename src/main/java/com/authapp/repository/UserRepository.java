package com.authapp.repository;

import com.authapp.entity.User;
import com.authapp.enums.CommonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndStatus(String username, CommonStatus status);
    Optional<User> findByUsername(String username);

    Stream<User> findByStatus(CommonStatus status);

    @Query("SELECT u FROM User u WHERE u.status=:status")
    Stream<User> findByStatusCustom(@Param("status") CommonStatus status);

    @Query("SELECT u FROM User u WHERE u.username <> 'admin'")
    List<User> test();
}