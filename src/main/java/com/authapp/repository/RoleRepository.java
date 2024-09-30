package com.authapp.repository;

import com.authapp.entity.Role;
import com.authapp.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByUserRole(UserRole userRole);
}